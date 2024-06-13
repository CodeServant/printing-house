package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Order

internal interface OrderDAO : JpaRepository<Order, Int>, OrderDAOCustom {
    fun findOrdersByCheckedFalse(): List<Order>
}