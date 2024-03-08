package pl.macia.printinghouse.web.dao

import io.kvision.rest.*
import pl.macia.printinghouse.web.authorize

class WorkflowStageStopDao {
    private val url = "http://localhost:8080/api/workflow-stage-stop"

    fun markWorkflowStageAsDone(
        wssId: Int,
        onFulfilled: (RestResponse<dynamic>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val prepUrl = "$url/$wssId?wwsDone=true"
        val premise = restClient.requestDynamic(prepUrl) {
            authorize()
            method = HttpMethod.PUT
        }
        premise.then(onFulfilled, onRejected)
    }
}