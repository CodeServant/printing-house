package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

const val tableSalesman = "Salesman"
const val salesmanId = "personId"

@Poko
@Entity
@Table(name = tableSalesman)
@PrimaryKeyJoinColumn(name = salesmanId)
class Salesman(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Employee(email, password, activeAccount, employed, name, surname, pseudoPESEL)