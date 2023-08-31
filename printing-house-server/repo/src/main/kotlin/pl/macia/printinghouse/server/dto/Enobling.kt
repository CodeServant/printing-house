package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Enobling.tableEnobling)
@Inheritance(strategy = InheritanceType.JOINED)
class Enobling(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = enoblingId)
    var id: Int?,
    @field:Size(max = 100)
    @field:NotNull
    @field:NotBlank
    @Column(name = enoblingName)
    var name: String,
    @field:Size(max = 500)
    @field:Nullable
    @Column(name = enoblingDescription)
    var description: String?
) {
    constructor(name: String, description: String?) : this(null, name, description)

    companion object {
        const val tableEnobling = "Enobling"
        const val enoblingId = "id"
        const val enoblingName = "name"
        const val enoblingDescription = "description"
    }
}