package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
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
    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST])
    @JoinColumn(name = ROLE_ID, referencedColumnName = Role.ID)
    var role: Role,
    @field:Size(max = 100)
    @field:NotBlank
    @field:NotNull
    @Column(name = NAME)
    var name: String,
    @field:NotEmpty
    @ManyToMany
    @JoinTable(
        name = MANAGER,
        joinColumns = [JoinColumn(name = MANAGER_WORKFLOW_STAGE_ID)],
        inverseJoinColumns = [JoinColumn(name = MANAGER_EMPLOYEE_ID)]
    )
    var workflowStageManagers: MutableSet<Worker>
) {
    companion object {
        const val TABLE_NAME = "WorkflowStage"
        const val ID = "id"
        const val ROLE_ID = "roleId"
        const val NAME = "name"
        const val MANAGER = "WorkflowStageManager"
        const val MANAGER_WORKFLOW_STAGE_ID = "workflowStage"
        const val MANAGER_EMPLOYEE_ID = "employeeId"
    }

    constructor(role: Role, name: String, workflowStageManagers: MutableSet<Worker>) : this(
        null,
        role,
        name,
        workflowStageManagers
    )
}