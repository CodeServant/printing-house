package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val tableWorkflowStage = "WorkflowStage"
const val workflowStageId = "id"
const val workflowStageRoleId = "roleId"
const val workflowStageName = "name"

@Poko
@Entity
@Table(name = tableWorkflowStage)
class WorkflowStage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = workflowStageId)
    var id: Int?,
    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST])
    @JoinColumn(name = workflowStageRoleId, referencedColumnName = roleId)
    var role: Role,
    @field:Size(max = 100)
    @field:NotBlank
    @field:NotNull
    @Column(name = workflowStageName)
    var name: String,
) {
    constructor(role: Role, name: String) : this(null, role, name)
}