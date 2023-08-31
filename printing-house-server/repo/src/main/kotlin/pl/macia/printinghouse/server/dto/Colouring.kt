package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.validator.constraints.Range

@Poko
@Entity
@Table(name = Colouring.tableColouring)
internal final class Colouring(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = colouringId)
    var id: Byte?,
    @Column(name = colouringFirstSide)
    @field:PositiveOrZero
    @field:Range(max = 4)
    var firstSide: Byte,
    @Column(name = colouringSecondSide)
    @field:PositiveOrZero
    @field:Range(max = 4)
    var secondSide: Byte
) {
    init {
        if (this.firstSide < this.secondSide) {
            val tmp = this.firstSide
            this.firstSide = this.secondSide
            this.secondSide = tmp
        }
    }

    constructor(firstSide: Byte, secondSide: Byte) : this(null, firstSide, secondSide)

    companion object {
        const val tableColouring = "Colouring"
        const val colouringId = "id"
        const val colouringFirstSide = "firstSide"
        const val colouringSecondSide = "secondSide"
    }
}