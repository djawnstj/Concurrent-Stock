package com.djawnstj.stock.service

import com.djawnstj.stock.domain.Stock
import com.djawnstj.stock.repository.StockRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class StockServiceTest {
    @Autowired
    private lateinit var stockRepository: StockRepository
    @Autowired
//    private lateinit var stockService: StockService
    private lateinit var stockService: PessimisticLockStockService

    @BeforeEach
    fun setUp() {
        stockRepository.saveAndFlush(Stock(-1L, 1L, 100L))
    }

    @AfterEach
    fun tearDown() {
        stockRepository.deleteAll()
    }

    @Test
    fun `재고감소`() {
        // when
        stockService.decrease(1L, 1L)

        // then
        val stock = stockRepository.findByIdOrNull(1L)!!
        assertThat(stock.getQuantity()).isEqualTo(99)
    }

    @Test
    fun `동시에 100개의 요청`() {
        // given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        // when
        repeat(threadCount) {
            executorService.submit {
                try {
                    stockService.decrease(1L, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        val stock = stockRepository.findByIdOrNull(1L)!!
        assertThat(stock.getQuantity()).isEqualTo(0)
    }
}
