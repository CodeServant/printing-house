package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Poko
@Entity
@Table(name = WorkflowStageStop.tableWorkflowStageStop)
class WorkflowStageStop internal constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = workflowStageStopId)
    var id: Int?,
    @Column(name = workflowStageStopComment)
    @field:Size(max = 500)
    var comment: String?,
    @Column(name = workflowStageStopCreateTime)
    var createTime: LocalDateTime,
    @Column(name = workflowStageStopAssignTime)
    @field:Nullable
    var assignTime: LocalDateTime?,
    @OneToOne
    @JoinColumn(name = workflowStageStopWorker, referencedColumnName = Worker.workerId)
    @field:Nullable
    var worker: Worker?,
    @OneToOne
    @JoinColumn(name = workflowStageStopWorkflowStage, referencedColumnName = WorkflowStage.workflowStageId)
    var workflowStage: WorkflowStage,
    @Column(name = workflowStageStopLastWorkflowStage)
    var lastWorkflowStage: Boolean,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = workflowStageStopOrder, nullable = false)
    val order: Order

) {
    companion object {
        const val tableWorkflowStageStop = "WorkflowStageStop"
        const val workflowStageStopId = "id"
        const val workflowStageStopComment = "comment"
        const val workflowStageStopCreateTime = "createTime"
        const val workflowStageStopAssignTime = "assignTime"
        const val workflowStageStopWorker = "worker"
        const val workflowStageStopOrder = "`order`"
        const val workflowStageStopWorkflowStage = "workflowStage"
        const val workflowStageStopLastWorkflowStage = "lastWorkflowStage"
        const val workflowStageStopOrderField = "order"
    }

    internal constructor(
        comment: String?,
        createTime: LocalDateTime,
        assignTime: LocalDateTime?,
        worker: Worker?,
        workflowStage: WorkflowStage,
        lastWorkflowStage: Boolean,
        order: Order
    ) : this(
        null, comment, createTime, assignTime, worker, workflowStage, lastWorkflowStage, order
    )
}