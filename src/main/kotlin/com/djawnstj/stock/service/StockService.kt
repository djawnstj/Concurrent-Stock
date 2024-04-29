package com.djawnstj.stock.service

import com.djawnstj.stock.repository.StockRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
    private val stockRepository: StockRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun decrease(id: Long, quantity: Long) {
        val stock = stockRepository.findByIdOrNull(id)!!
        stock.decrease(quantity)
        stockRepository.saveAndFlush(stock)
    }

}
