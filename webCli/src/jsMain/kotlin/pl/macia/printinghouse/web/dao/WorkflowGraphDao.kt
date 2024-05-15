package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.request.WorkflowGraphReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.web.clientConfig

class WorkflowGraphDao {
    private val url = "${clientConfig.serviceUrl}/api/workflowGraphs"
    private val dullDao = DullDao(url)
    fun allWorkflowGraphs(
        onFulfilled: (List<WorkflowGraphResp>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getAllDull(onFulfilled, onRejected)

    fun newWorkflowGraph(
        workflowGraphReq: WorkflowGraphReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(workflowGraphReq, onFulfilled, onRejected)
}