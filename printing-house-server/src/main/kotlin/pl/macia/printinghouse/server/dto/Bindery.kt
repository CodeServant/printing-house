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
@Table(name = "Bindery")
class Bindery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int?,
    @field:NotNull(message = "null property not allowed")
    @field:NotBlank(message = "blank property not allowed")
    @field:Size(max = 200)
    @Column(name = "name")
    var name: String?
) {
    constructor(name: String) : this(null, name)

    override fun toString(): String {
        return "{$id, $name}"
    }
}