package com.djawnstj.stock.facade

import com.djawnstj.stock.repository.RockRepository
import com.djawnstj.stock.service.StockService
import org.springframework.stereotype.Component

@Component
class NamedLockStockFacade(
    private val lockRepository: RockRepository,
    private val stockService: StockService
) {

    fun decrease(id: Long, quantity: Long) {
        try {
            lockRepository.getLock(id.toString())
            stockService.decrease(id, quantity)
        } finally {
            lockRepository.release(id.toString())
        }
    }

}
