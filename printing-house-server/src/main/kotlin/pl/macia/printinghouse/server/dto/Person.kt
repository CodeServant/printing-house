package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val tablePerson = "Person"
const val personId = "id"
const val personName = "name"
const val personSurname = "surname"
const val personPseudoPESEL = "pseudoPESEL"

@Poko
@Entity
@Table(name = tablePerson)
open class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = personId)
    val id: Int?,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = personName)
    val name: String,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 300)
    @Column(name = personSurname)
    val surname: String,
    @field:NotNull
    @field:Size(min = 11, max = 11)
    @Column(name = personPseudoPESEL)
    val pseudoPESEL: String
) {
    override fun toString(): String {
        return "{$personId: $id, $personName: $name, $personSurname: $surname, $personPseudoPESEL: $pseudoPESEL}"
    }
}