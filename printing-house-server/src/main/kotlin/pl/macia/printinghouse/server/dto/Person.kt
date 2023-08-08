package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = "Person")
open class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int?,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = "name")
    val name: String,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 300)
    @Column(name = "surname")
    val surname: String,
    @field:NotNull
    @field:Size(min = 11, max = 11)
    @Column(name = "pseudoPESEL")
    val pseudoPESEL: String
) {
    override fun toString(): String {
        return "{id: $id, name: $name, surname: $surname, pseudoPESEL: $pseudoPESEL}"
    }
}