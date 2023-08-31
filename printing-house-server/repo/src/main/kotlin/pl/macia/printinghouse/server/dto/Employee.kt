package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Employee.TABLE_NAME)
@PrimaryKeyJoinColumn(name = Employee.ID)
@Inheritance(strategy = InheritanceType.JOINED)
internal class Employee(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = EMAIL, referencedColumnName = Email.ID)
    var email: Email,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 68, min = 68)
    @Column(name = PASSWORD)
    var password: String,
    @field:NotNull
    @Column(name = ACTIVE_ACCOUNT)
    var activeAccount: Boolean,
    @field:NotNull
    @Column(name = EMPLOYED)
    var employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Person(name, surname, pseudoPESEL) {
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = EMPLOYEE_ROLE,
        joinColumns = [JoinColumn(name = ROLE_ID)],
        inverseJoinColumns = [JoinColumn(name = EMP_ID)]
    )
    var roles = mutableSetOf<Role>()

    companion object {
        const val TABLE_NAME = "Employee"
        const val ID = "personId"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ACTIVE_ACCOUNT = "activeAccount"
        const val EMPLOYED = "employed"
        const val EMPLOYEE_ROLE = "EmploeeRole"
        const val EMP_ID = "empId"
        const val ROLE_ID = "roleId"
    }
}