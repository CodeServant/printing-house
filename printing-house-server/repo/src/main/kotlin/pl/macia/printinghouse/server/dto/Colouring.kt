package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.validator.constraints.Range

@Poko
@Entity
@Table(name = Colouring.TABLE_NAME)
internal final class Colouring(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Byte?,
    @Column(name = FIRST_SIDE)
    @field:PositiveOrZero
    @field:Range(max = 4)
    var firstSide: Byte,
    @Column(name = SECOND_SIDE)
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
        const val TABLE_NAME = "Colouring"
        const val ID = "id"
        const val FIRST_SIDE = "firstSide"
        const val SECOND_SIDE = "secondSide"
    }
}