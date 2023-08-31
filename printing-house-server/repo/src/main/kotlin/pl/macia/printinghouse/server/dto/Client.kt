package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern

@Poko
@Entity
@Table(name = Client.TABLE_NAME)
internal class Client(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = EMAIL, referencedColumnName = Email.ID)
    var email: Email?,
    @Column(name = PHONE_NUMBER)
    @field:Pattern(regexp = "^(\\+[1-9]{1,4} )?[0-9]{3,14}\$")
    var phoneNumber: String?
) {
    constructor(email: Email?, phoneNumber: String?) : this(null, email, phoneNumber)

    companion object {
        const val TABLE_NAME = "Client"
        const val ID = "id"
        const val EMAIL = "email"
        const val PHONE_NUMBER = "phoneNumber"
    }
}