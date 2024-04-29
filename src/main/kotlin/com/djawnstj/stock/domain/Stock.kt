package com.djawnstj.stock.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version

@Entity
class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long,
    private var productId: Long,
    private var quantity: Long,
    @Version
    private var version: Long = -1
) {

    fun getQuantity(): Long = this.quantity

    fun decrease(quantity: Long) {
        if (this.quantity - quantity < 0) {
            throw RuntimeException("재고는 0개 미만이 될 수 없습니다.")
        }

        this.quantity -= quantity
    }
}
