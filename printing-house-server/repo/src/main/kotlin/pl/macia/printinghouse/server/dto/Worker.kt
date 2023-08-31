package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = Worker.TABLE_NAME)
@PrimaryKeyJoinColumn(name = Worker.ID)
internal class Worker(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String,
    @ManyToMany(mappedBy = WORKER_IS_MANAGER_OF)
    var isManagerOf: MutableList<WorkflowStage> = mutableListOf()
) : Employee(email, password, activeAccount, employed, name, surname, pseudoPESEL) {
    companion object {
        const val TABLE_NAME = "Worker"
        const val ID = "personId"
        const val WORKER_IS_MANAGER_OF = "workflowStageManagers"
    }
}