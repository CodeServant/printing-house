package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = Salesman.tableSalesman)
@PrimaryKeyJoinColumn(name = Salesman.salesmanId)
class Salesman(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Employee(email, password, activeAccount, employed, name, surname, pseudoPESEL) {
    companion object {
        const val tableSalesman = "Salesman"
        const val salesmanId = "personId"
    }
}