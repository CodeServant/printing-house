package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Poko
@Entity
@Table(name = WorkflowDirGraph.TAB_NAME)
internal class WorkflowDirGraph(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    var id: Int?,
    @Column(name = CREATION_TIME, nullable = false)
    var creationTime: LocalDateTime,
    @Column(name = COMMENT, nullable = true)
    @field:Size(max = 500)
    var comment: String?,
    @Column(name = NAME, nullable = false)
    @field:Size(max = 300)
    @field:NotBlank
    var name: String,
    @Column(name = CHANGED_TIME, nullable = true)
    var changedTime: LocalDateTime?,
    @OneToMany(
        mappedBy = WorkflowDirEdge.GRAPH_FIELD,
        cascade = [CascadeType.ALL]
    )
    @field:Size(min = 1)
    val edges: MutableList<WorkflowDirEdge> = mutableListOf()
) {
    companion object {
        const val TAB_NAME = "WorkflowDirGraph"
        const val ID = "id"
        const val CREATION_TIME = "creationTime"
        const val COMMENT = "comment"
        const val NAME = "name"
        const val CHANGED_TIME = "changedTime"
    }

    fun addEdge(
        v1: WorkflowStage,
        v2: WorkflowStage
    ): WorkflowDirEdge {
        return WorkflowDirEdge(null, this, v1, v2).apply(edges::add)
    }
}