package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Person.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
internal class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    val id: Int?,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = NAME)
    var name: String,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 300)
    @Column(name = SURNAME)
    var surname: String,
    @field:NotNull
    @field:Size(min = 11, max = 11)
    @Column(name = PSEUDO_PESEL)
    var pseudoPESEL: String
) {
    companion object {
        const val TABLE_NAME = "Person"
        const val ID = "id"
        const val NAME = "name"
        const val SURNAME = "surname"
        const val PSEUDO_PESEL = "pseudoPESEL"
    }

    constructor(name: String, surname: String, pseudoPESEL: String) : this(null, name, surname, pseudoPESEL)

    override fun toString(): String {
        return "{$ID: $id, $NAME: $name, $SURNAME: $surname, $PSEUDO_PESEL: $pseudoPESEL}"
    }
}