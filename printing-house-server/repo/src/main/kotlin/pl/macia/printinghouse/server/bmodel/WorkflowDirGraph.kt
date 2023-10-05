package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime

interface WorkflowDirGraph {
    var wGraphId: Int?
    var creationTime: LocalDateTime
    var comment: String?
    var name: String
    var changedTime: LocalDateTime?
    val edge: MutableList<WorkflowDirEdge>
    fun addEdge(v1: WorkflowStage, v2: WorkflowStage): WorkflowDirEdge
}