package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.web.clientConfig

class WorkflowGraphDao {
    private val url = "${clientConfig.serviceUrl}/api/workflowGraphs"
    private val dullDao = DullDao(url)
    fun allWorkflowGraphs(
        onFulfilled: (List<WorkflowGraphResp>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getAllDull(onFulfilled, onRejected)
}