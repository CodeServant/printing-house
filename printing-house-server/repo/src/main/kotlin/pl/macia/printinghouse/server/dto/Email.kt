package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Email as AEmail
import jakarta.validation.constraints.NotNull

@Poko
@Entity
@Table(name = Email.TABLE_NAME)
internal class Email(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int? = null,
    @field:NotNull
    @field:AEmail
    @Column(name = EMAIL)
    var email: String
) {
    constructor(email: String) : this(null, email)

    companion object {
        const val TABLE_NAME = "Email"
        const val ID = "id"
        const val EMAIL = "email"
    }
}
