package pl.macia.printinghouse.server.bmodel

sealed interface WorkflowStage {
    var workflowStageid: Int?
    var name: String
    val workflowManagers: MutableList<Worker>
    var role: Role
}