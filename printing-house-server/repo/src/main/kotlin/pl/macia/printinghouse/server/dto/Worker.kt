package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = Worker.tableWorker)
@PrimaryKeyJoinColumn(name = Worker.workerId)
class Worker(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String,
    @ManyToMany(mappedBy = workerIsManagerOf)
    var isManagerOf: MutableList<WorkflowStage> = mutableListOf()
) : Employee(email, password, activeAccount, employed, name, surname, pseudoPESEL) {
    companion object {
        const val tableWorker = "Worker"
        const val workerId = "personId"
        const val workerIsManagerOf = "workflowStageManagers"
    }
}