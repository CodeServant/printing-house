package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = PaperType.tablePaperType)
internal class PaperType(
    @Id
    @Column(name = paperTypeId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @field:Size(max = 200)
    @field:NotBlank
    @Column(name = paperTypeName)
    var name: String,
    @field:Size(max = 30)
    @field:NotBlank
    @Column(name = paperTypeShortName)
    var shortName: String
) {
    companion object {
        const val tablePaperType = "PaperType"
        const val paperTypeId = "id"
        const val paperTypeName = "name"
        const val paperTypeShortName = "shortName"
    }

    constructor(name: String, shortName: String) : this(null, name, shortName)
}