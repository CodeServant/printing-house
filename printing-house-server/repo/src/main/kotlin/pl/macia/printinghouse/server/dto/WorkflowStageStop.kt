package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Poko
@Entity
@Table(name = WorkflowStageStop.TABLE_NAME)
internal class WorkflowStageStop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @Column(name = COMMENT)
    @field:Size(max = 500)
    var comment: String?,
    @Column(name = CREATION_TIME)
    var createTime: LocalDateTime,
    @Column(name = ASSIGN_TIME)
    @field:Nullable
    var assignTime: LocalDateTime?,
    @OneToOne
    @JoinColumn(name = WORKER, referencedColumnName = Worker.ID)
    @field:Nullable
    var worker: Worker?,
    @OneToOne
    @JoinColumn(name = WORKFLOW_STAGE, referencedColumnName = WorkflowStage.ID)
    var workflowStage: WorkflowStage,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ORDER, nullable = false)
    val order: Order,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = WORKFLOW_EDGE_ID, nullable = false)
    var workflowGraphEdge: WorkflowDirEdge
) {
    companion object {
        const val TABLE_NAME = "WorkflowStageStop"
        const val ID = "id"
        const val COMMENT = "comment"
        const val CREATION_TIME = "createTime"
        const val ASSIGN_TIME = "assignTime"
        const val WORKER = "worker"
        const val ORDER = "`order`"
        const val WORKFLOW_STAGE = "workflowStage"
        const val ORDER_FIELD = "order"
        const val WORKFLOW_EDGE_ID = "workflowEdgeId"
    }

    constructor(
        comment: String?,
        createTime: LocalDateTime,
        assignTime: LocalDateTime?,
        worker: Worker?,
        workflowStage: WorkflowStage,
        wrkflowDirEdge: WorkflowDirEdge,
        order: Order
    ) : this(
        null, comment, createTime, assignTime, worker, workflowStage, order, wrkflowDirEdge
    )
}