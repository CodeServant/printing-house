package pl.macia.printinghouse.server.bmodel

/**
 * Since someone have to conduct [WorkflowStage] there have to be at least one [Worker] in [workflowManagers].
 */
sealed interface WorkflowStage {
    var workflowStageid: Int?
    var name: String
    val workflowManagers: MutableList<Worker>
}