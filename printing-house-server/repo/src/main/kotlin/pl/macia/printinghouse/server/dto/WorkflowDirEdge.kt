package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = WorkflowDirEdge.NAME)
internal class WorkflowDirEdge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = WorkflowStageStop.ID)
    var id: Int?
) {
    companion object {
        const val NAME = "WorkflowDirEdge"
        const val ID = "id"
    }
}