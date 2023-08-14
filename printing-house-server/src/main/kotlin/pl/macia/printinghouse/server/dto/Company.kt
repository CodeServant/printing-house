package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

const val tableCompany = "Company"
const val companyId = "id"
const val companyName = "name"
const val companyNIP = "NIP"
const val companyClientId = "clientId"

@Poko
@Entity
@Table(name = tableCompany)
class Company(
    @Id
    @Column(name = companyId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @Column(name = companyName)
    @field:NotBlank
    @field:NotNull
    var name: String,
    @Column(name = companyNIP)
    @field:NotNull
    @field:Pattern(regexp = "\\d{10}")
    var nip: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = companyClientId, referencedColumnName = clientId)
    var client: Client? = null
) {
    constructor(name: String, nip: String, client: Client?) : this(null, name, nip, client)
}