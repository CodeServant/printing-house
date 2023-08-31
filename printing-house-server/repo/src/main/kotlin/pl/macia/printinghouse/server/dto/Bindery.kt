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
@Table(name = Bindery.tableBindery)
internal class Bindery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = binderyId)
    var id: Int?,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = binderyName)
    var name: String
) {
    constructor(name: String) : this(null, name)

    override fun toString(): String {
        return "{$binderyId: $id, $binderyName: $name}"
    }

    companion object {
        const val tableBindery = "Bindery"
        const val binderyId = "id"
        const val binderyName = "name"
    }
}