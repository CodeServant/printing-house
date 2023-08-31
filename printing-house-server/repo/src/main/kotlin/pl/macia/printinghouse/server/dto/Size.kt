package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

const val tableSize = "Size"
const val sizeId = "id"
const val sizeName = "name"
const val sizeWidth = "width"
const val sizeHeigth = "heigth"

@Poko
@Entity
@Table(name = tableSize)
class Size(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = sizeId)
    var id: Int?,
    @field:Size(min = 1, max = 50)
    @Column(name = sizeName)
    var name: String?,
    @field:NotNull
    @field:Positive
    @Column(name = sizeWidth)
    var width: Double,
    @field:NotNull
    @field:Positive
    @Column(name = sizeHeigth)
    var heigth: Double
) {
    constructor(name: String, width: Double, heigth: Double) : this(null, name, width, heigth)
    constructor(width: Double, heigth: Double) : this(null, null, width, heigth)
}