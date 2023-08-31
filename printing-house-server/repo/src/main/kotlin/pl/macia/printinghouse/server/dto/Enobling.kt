package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Enobling.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
internal class Enobling(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:Size(max = 100)
    @field:NotNull
    @field:NotBlank
    @Column(name = NAME)
    var name: String,
    @field:Size(max = 500)
    @field:Nullable
    @Column(name = DESCRIPTION)
    var description: String?
) {
    constructor(name: String, description: String?) : this(null, name, description)

    companion object {
        const val TABLE_NAME = "Enobling"
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}