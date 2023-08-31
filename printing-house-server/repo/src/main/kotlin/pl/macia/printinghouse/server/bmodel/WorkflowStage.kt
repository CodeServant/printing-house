package pl.macia.printinghouse.server.bmodel

interface WorkflowStage {
    var workflowStageid: Int?
    var name: String
    var workflowManagers: List<Worker>
    var role: Role
}