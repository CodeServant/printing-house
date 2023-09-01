package pl.macia.printinghouse.server.bmodel

sealed interface WorkflowStage {
    var workflowStageid: Int?
    var name: String
    var workflowManagers: List<Worker>
    var role: Role
}