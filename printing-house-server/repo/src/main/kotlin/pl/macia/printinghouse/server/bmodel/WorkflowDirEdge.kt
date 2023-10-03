package pl.macia.printinghouse.server.bmodel

interface WorkflowDirEdge {
    var wEdgeId: Int?
    var grapf: WorkflowDirGraph
    var v1: WorkflowStage
    var v2: WorkflowStage
}