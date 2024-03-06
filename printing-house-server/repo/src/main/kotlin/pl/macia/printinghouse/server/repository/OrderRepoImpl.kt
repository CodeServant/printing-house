package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Order
import pl.macia.printinghouse.server.bmodel.OrderImpl
import pl.macia.printinghouse.server.dao.OrderDAO
import pl.macia.printinghouse.server.dto.Order as POrder

@Repository
internal class OrderRepoImpl : OrderIntRepo {
    @Autowired
    lateinit var dao: OrderDAO
    fun POrder.toBiz(): OrderImpl {
        return OrderImpl(this)
    }

    override fun save(obj: Order): Order {
        obj as OrderImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Order? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findNotCompletedByLastAssignee(lastAssignee: Int): List<Order> {
        val orders = dao.findNotCompletedByLastAssignee(lastAssignee)
        return orders.map {
            it.toBiz()
        }
    }

    override fun findByWssId(wssId: Int): Order? {
        return dao.findByWorkflowStageStopId(wssId)?.toBiz()
    }
}