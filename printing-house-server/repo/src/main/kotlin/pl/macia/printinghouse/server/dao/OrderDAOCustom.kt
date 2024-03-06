package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Order

internal interface OrderDAOCustom {
    /**
     * Current [Order]s (not withdrawn or completed), which the worker is currently assigned for.
     */
    fun findCurrentByLastAssignee(lastAssignee: Int): List<Order>
    fun findByWorkflowStageStopId(wssId: Int): Order?
}