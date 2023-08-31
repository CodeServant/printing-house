package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size as ASize

@Poko
@Entity
@Table(name = Size.TABLE_NAME)
internal class Size(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:ASize(min = 1, max = 50)
    @Column(name = NAME)
    var name: String?,
    @field:NotNull
    @field:Positive
    @Column(name = WIDTH)
    var width: Double,
    @field:NotNull
    @field:Positive
    @Column(name = HEIGTH)
    var heigth: Double
) {
    constructor(name: String, width: Double, heigth: Double) : this(null, name, width, heigth)
    constructor(width: Double, heigth: Double) : this(null, null, width, heigth)

    companion object {
        const val TABLE_NAME = "Size"
        const val ID = "id"
        const val NAME = "name"
        const val WIDTH = "width"
        const val HEIGTH = "heigth"
    }
}