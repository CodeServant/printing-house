package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val tableRole = "Role"
const val roleId = "id"
const val roleName = "name"

const val roleRoles = "roles"

@Poko
@Entity
@Table(name = tableRole)
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = roleId)
    var id: Int?,
    @field:NotBlank
    @field:Size(max = 200)
    @Column(name = roleName)
    var name: String
) {

    @ManyToMany(mappedBy = roleRoles, fetch = FetchType.LAZY)
    var employees = mutableListOf<Employee>()

    constructor(name: String) : this(null, name)
}