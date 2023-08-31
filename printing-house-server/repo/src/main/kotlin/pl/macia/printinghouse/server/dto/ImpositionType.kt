package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = ImpositionType.TABLE_NAME)
internal class ImpositionType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @Column(name = NAME)
    @field:NotBlank
    @field:Size(max = 10)
    var name: String
) {
    constructor(name: String) : this(null, name)

    companion object {
        const val TABLE_NAME = "ImpositionType"
        const val ID = "id"
        const val NAME = "name"
    }
}