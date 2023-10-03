package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime

interface WorkflowDirGraph {
    // todo implement this and workflowDirEdge
    // todo create repo for edge and graph
    // todo test repo and impleentation
    var wGraphId: Int?
    var creationTime: LocalDateTime
    var comment: String?
    var name: String
    var changedTime: LocalDateTime?
    val edge: MutableList<WorkflowDirEdge>
}