package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.WorkflowGraphResp

class WorkflowGraphDao {
    private val url = "http://localhost:8080/api/workflowGraphs"
    private val dullDao = DullDao(url)
    fun allWorkflowGraphs(
        onFulfilled: (List<WorkflowGraphResp>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getAllDull(onFulfilled, onRejected)
}