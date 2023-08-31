package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Poko
@Entity
@Table(name = Company.TABLE_NAME)
internal class Company(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @Column(name = NAME)
    @field:NotBlank
    @field:NotNull
    var name: String,
    @Column(name = NIP)
    @field:NotNull
    @field:Pattern(regexp = "\\d{10}")
    var nip: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = CLIENT_ID, referencedColumnName = Client.ID)
    var client: Client? = null
) {
    constructor(name: String, nip: String, client: Client?) : this(null, name, nip, client)

    companion object {
        const val TABLE_NAME = "Company"
        const val ID = "id"
        const val NAME = "name"
        const val NIP = "NIP"
        const val CLIENT_ID = "clientId"
    }
}