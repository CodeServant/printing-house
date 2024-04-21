package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Order

@Repository
internal class OrderDAOCustomImpl : OrderDAOCustom {
    @Autowired
    private lateinit var em: EntityManager
    override fun findNotCompletedByLastAssignee(lastAssignee: Int): List<Order> {
        val query =
            em.createQuery(
                """SELECT wss.order FROM WorkflowStageStop as wss WHERE wss.assignTime = (SELECT MAX(wss2.assignTime) FROM WorkflowStageStop as wss2 WHERE wss.order=wss2.order) AND wss.worker.id=:workerId AND wss.completionTime IS NULL""", // the most recent assigns in orders
                Order::class.java
            )
        query.setParameter("workerId", lastAssignee)
        return query.resultList
    }

    override fun findByWorkflowStageStopId(wssId: Int): Order? {
        val q: TypedQuery<Order?> = em.createQuery(
            """SELECT wss.order FROM WorkflowStageStop as wss WHERE wss.id=:wssId """, // the most recent assigns in orders
            Order::class.java
        )
        q.setParameter("wssId", wssId)
        return try {
            q.singleResult
        } catch (e: NoResultException) {
            null
        }
    }

    override fun notAssignedWorkflowStage(stageId: Int): List<Order> {
        val query = em.createQuery(
            """SELECT wss.order FROM WorkflowStageStop as wss WHERE wss.workflowGraphEdge.v1.id=:stageId AND wss.assignTime IS NULL AND wss.worker IS NULL""",
            Order::class.java
        )
        query.setParameter("stageId", stageId)
        return query.resultList
    }
}
