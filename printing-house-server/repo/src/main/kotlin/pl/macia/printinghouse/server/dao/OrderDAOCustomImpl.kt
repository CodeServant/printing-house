package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Order

@Repository
internal class OrderDAOCustomImpl : OrderDAOCustom {
    @Autowired
    private lateinit var em: EntityManager
    override fun findCurrentByLastAssignee(lastAssignee: Int): List<Order> {
        val query =
            em.createQuery(
                """SELECT wss.order FROM WorkflowStageStop as wss WHERE wss.assignTime = (SELECT MAX(wss2.assignTime) FROM WorkflowStageStop as wss2 WHERE wss.order=wss2.order) AND wss.worker.id=:workerId""", // the most recent assigns in orders
                Order::class.java
            )
        query.setParameter("workerId", lastAssignee)
        return query.resultList
    }
}
