package pl.macia.printinghouse.server.bmodel

sealed interface Worker : Employee {
    val isManagerOf: MutableList<WorkflowStage>
}