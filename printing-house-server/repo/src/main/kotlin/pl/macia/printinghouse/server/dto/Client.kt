package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern

@Poko
@Entity
@Table(name = Client.tableClient)
class Client(
    @Id
    @Column(name = clientId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = clientEmail, referencedColumnName = Email.emailId)
    var email: Email?,
    @Column(name = clientPhoneNumber)
    @field:Pattern(regexp = "^(\\+[1-9]{1,4} )?[0-9]{3,14}\$")
    var phoneNumber: String?
) {
    constructor(email: Email?, phoneNumber: String?) : this(null, email, phoneNumber)

    companion object {
        const val tableClient = "Client"
        const val clientId = "id"
        const val clientEmail = "email"
        const val clientPhoneNumber = "phoneNumber"
    }
}