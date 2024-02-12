package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.WorkflowStageResp

class WorkflowStageDao {
    private val url = "http://localhost:8080/api/workflow-stages"
    private val dullDao = DullDao(url)
    fun allWorkflowStages(onFulfilled: (List<WorkflowStageResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)
}