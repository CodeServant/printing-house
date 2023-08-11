package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

const val tableWorker = "Worker"
const val workerId = "personId"

@Poko
@Entity
@Table(name = tableWorker)
@PrimaryKeyJoinColumn(name = workerId)
class Worker(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Employee(email, password, activeAccount, employed, name, surname, pseudoPESEL)