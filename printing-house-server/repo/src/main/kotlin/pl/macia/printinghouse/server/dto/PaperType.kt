package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = PaperType.TABLE_NAME)
internal class PaperType(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @field:Size(max = 200)
    @field:NotBlank
    @Column(name = NAME)
    var name: String,
    @field:Size(max = 30)
    @field:NotBlank
    @Column(name = SHORT_NAME)
    var shortName: String
) {
    companion object {
        const val TABLE_NAME = "PaperType"
        const val ID = "id"
        const val NAME = "name"
        const val SHORT_NAME = "shortName"
    }

    constructor(name: String, shortName: String) : this(null, name, shortName)
}