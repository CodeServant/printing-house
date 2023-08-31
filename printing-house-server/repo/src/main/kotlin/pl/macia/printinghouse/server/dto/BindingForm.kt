package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val tableBindingForm = "BindingForm"
const val bindingFormId = "id"
const val bindingFormName = "name"

@Poko
@Entity
@Table(name = tableBindingForm)
class BindingForm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = bindingFormId)
    val id: Int?,
    @field:Size(max = 200)
    @field:NotBlank
    @Column(name = bindingFormName)
    var name: String
) {
    constructor(name: String) : this(null, name)
}