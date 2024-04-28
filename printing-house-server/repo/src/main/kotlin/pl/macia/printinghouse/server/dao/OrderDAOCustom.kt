package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Order

internal interface OrderDAOCustom {
    /**
     * Current [Order]s (not withdrawn or completed), which the worker is currently assigned for and not completed the task.
     */
    fun findNotCompletedByLastAssignee(lastAssignee: Int): List<Order>
    fun findByWorkflowStageStopId(wssId: Int): Order?

    /**
     * Orders that are not assigned but belongs to specific [pl.macia.printinghouse.server.dto.WorkflowStage].
     * @param areChecked fetch those that are marked checked by salesman, if false get unchecked, if null ignore checked field
     */
    fun notAssignedWorkflowStage(stageId: Int, areChecked: Boolean? = null): List<Order>

    /**
     * [Order]s that are completed but not finalize by the [pl.macia.printinghouse.server.dto.Salesman] (there is no unassigned [pl.macia.printinghouse.server.dto.WorkflowStageStop])
     * @param [pl.macia.printinghouse.server.dto.Salesman] email for which we want to select orders
     */
    fun pathCompletedOrders(email: String): List<Order>
}