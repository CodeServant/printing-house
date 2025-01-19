package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = WorkflowStage.TABLE_NAME)
internal class WorkflowStage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:Size(max = 100)
    @field:NotBlank
    @field:NotNull
    @Column(name = NAME)
    var name: String,
    @ManyToMany
    @JoinTable(
        name = MANAGER,
        joinColumns = [JoinColumn(name = MANAGER_WORKFLOW_STAGE_ID)],
        inverseJoinColumns = [JoinColumn(name = MANAGER_EMPLOYEE_ID)]
    )
    var workflowStageManagers: MutableList<Worker>
) {
    companion object {
        const val TABLE_NAME = "WorkflowStage"
        const val ID = "id"
        const val NAME = "name"
        const val MANAGER = "WorkflowStageManager"
        const val MANAGER_WORKFLOW_STAGE_ID = "workflowStage"
        const val MANAGER_EMPLOYEE_ID = "employeeId"
    }

    constructor(name: String, workflowStageManagers: MutableList<Worker>) : this(
        null,
        name,
        workflowStageManagers
    )
}