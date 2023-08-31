package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Bindery.TABLE_NAME)
internal class Bindery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = NAME)
    var name: String
) {
    constructor(name: String) : this(null, name)

    override fun toString(): String {
        return "{$ID: $id, $NAME: $name}"
    }

    companion object {
        const val TABLE_NAME = "Bindery"
        const val ID = "id"
        const val NAME = "name"
    }
}