package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Email as AEmail
import jakarta.validation.constraints.NotNull

@Poko
@Entity
@Table(name = Email.tableEmail)
internal class Email(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = emailId)
    var id: Int? = null,
    @field:NotNull
    @field:AEmail
    @Column(name = emailEmail)
    var email: String
) {
    constructor(email: String) : this(null, email)

    companion object {
        const val tableEmail = "Email"
        const val emailId = "id"
        const val emailEmail = "email"
    }
}
