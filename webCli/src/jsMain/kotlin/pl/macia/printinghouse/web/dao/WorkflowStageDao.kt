package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestResponse
import pl.macia.printinghouse.request.WorkflowStageChangeReq
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkflowStageResp

class WorkflowStageDao {
    private val url = "http://localhost:8080/api/workflow-stages"
    private val dullDao = DullDao(url)
    fun allWorkflowStages(onFulfilled: (List<WorkflowStageResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newWorkflowStageReq(
        workflowStageReq: WorkflowStageReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(workflowStageReq, onFulfilled, onRejected)

    fun getWorkflowStage(
        id: Int,
        onFulfilled: (WorkflowStageResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun delWorkflowStage(
        id: Int,
        onFulfilled: (RestResponse<dynamic>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.delDullObj(id, onFulfilled, onRejected)

    fun changeWorkflowStage(
        id: Int,
        changeReq: WorkflowStageChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}