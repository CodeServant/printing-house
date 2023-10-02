package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime

sealed interface WorkflowStageStop {
    var wfssId: Int?
    var comment: String?
    var assignTime: LocalDateTime?
    var createTime: LocalDateTime
    var worker: Worker?
    var workflowStage: WorkflowStage
    val order: Order
}