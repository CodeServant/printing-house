package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = BindingForm.TABLE_NAME)
internal class BindingForm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:Size(max = 200)
    @field:NotBlank
    @Column(name = NAME)
    var name: String
) {
    constructor(name: String) : this(null, name)

    companion object {
        const val TABLE_NAME = "BindingForm"
        const val ID = "id"
        const val NAME = "name"
    }
}