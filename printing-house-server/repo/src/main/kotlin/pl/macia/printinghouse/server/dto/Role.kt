package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Role.TABLE_NAME)
internal class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = NAME)
    var name: String
) {

    @ManyToMany(mappedBy = ROLES, fetch = FetchType.LAZY)
    var employees = mutableListOf<Employee>()

    constructor(name: String) : this(null, name)

    companion object {
        const val TABLE_NAME = "Role"
        const val ID = "id"
        const val NAME = "name"
        const val ROLES = "roles"
    }
}