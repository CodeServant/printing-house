package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = ImpositionType.tableImpositionType)
internal class ImpositionType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = impositionTypeId)
    var id: Int?,
    @Column(name = impositionTypeName)
    @field:NotBlank
    @field:Size(max = 10)
    var name: String
) {
    constructor(name: String) : this(null, name)

    companion object {
        const val tableImpositionType = "ImpositionType"
        const val impositionTypeId = "id"
        const val impositionTypeName = "name"
    }
}