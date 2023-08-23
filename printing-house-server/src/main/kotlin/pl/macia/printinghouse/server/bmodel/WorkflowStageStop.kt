package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime

interface WorkflowStageStop {
    var wfssId: Int?
    var comment: String?
    var lastWorkflowStage: Boolean
    var assignTime: LocalDateTime?
    var createTime: LocalDateTime
    var worker: Worker?
    var workflowStage: WorkflowStage
    var order: Order
}