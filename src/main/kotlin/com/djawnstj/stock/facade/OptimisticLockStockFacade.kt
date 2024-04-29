package com.djawnstj.stock.facade

import com.djawnstj.stock.service.OptimisticLockStockService
import org.springframework.stereotype.Component

@Component
class OptimisticLockStockFacade(
    private val stockService: OptimisticLockStockService
) {

    fun decrease(id: Long, quantity: Long) {
        while (true) {
            try {
                stockService.decrease(id, quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(50)
            }
        }
    }

}
