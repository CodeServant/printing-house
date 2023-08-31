package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

const val tableEmail = "Email"
const val emailId = "id"
const val emailEmail = "email"

@Poko
@Entity
@Table(name = tableEmail)
class Email(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = emailId)
    var id: Int? = null,
    @field:NotNull
    @field:Email
    @Column(name = emailEmail)
    var email: String
) {
    constructor(email: String) : this(null, email)
}
