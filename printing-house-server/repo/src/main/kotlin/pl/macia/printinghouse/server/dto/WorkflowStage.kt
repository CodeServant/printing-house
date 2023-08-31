package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = WorkflowStage.tableWorkflowStage)
class WorkflowStage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = workflowStageId)
    var id: Int?,
    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST])
    @JoinColumn(name = workflowStageRoleId, referencedColumnName = Role.roleId)
    var role: Role,
    @field:Size(max = 100)
    @field:NotBlank
    @field:NotNull
    @Column(name = workflowStageName)
    var name: String,
    @field:NotEmpty
    @ManyToMany
    @JoinTable(
        name = tableWorkflowStageManager,
        joinColumns = [JoinColumn(name = workflowStageMgrWrkflStId)],
        inverseJoinColumns = [JoinColumn(name = workflowStageMgrEmpId)]
    )
    var workflowStageManagers: MutableSet<Worker>
) {
    companion object {
        const val tableWorkflowStage = "WorkflowStage"
        const val workflowStageId = "id"
        const val workflowStageRoleId = "roleId"
        const val workflowStageName = "name"
        const val tableWorkflowStageManager = "WorkflowStageManager"
        const val workflowStageMgrWrkflStId = "workflowStage"
        const val workflowStageMgrEmpId = "employeeId"
    }

    constructor(role: Role, name: String, workflowStageManagers: MutableSet<Worker>) : this(
        null,
        role,
        name,
        workflowStageManagers
    )
}