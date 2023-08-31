package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Employee.tableEmployee)
@PrimaryKeyJoinColumn(name = Employee.employeeId)
@Inheritance(strategy = InheritanceType.JOINED)
class Employee(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = employeeEmail, referencedColumnName = Email.emailId)
    var email: Email,
    @field:NotNull
    @field:NotBlank
    @field:Size(max = 68, min = 68)
    @Column(name = employeePassword)
    var password: String,
    @field:NotNull
    @Column(name = employeeActiveAccount)
    var activeAccount: Boolean,
    @field:NotNull
    @Column(name = employeeEmployed)
    var employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
) : Person(name, surname, pseudoPESEL) {
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = employeeEmploeeRole,
        joinColumns = [JoinColumn(name = employeeRoleId)],
        inverseJoinColumns = [JoinColumn(name = employeeEmpId)]
    )
    var roles = mutableSetOf<Role>()

    companion object {
        const val tableEmployee = "Employee"
        const val employeeId = "personId"
        const val employeeEmail = "email"
        const val employeePassword = "password"
        const val employeeActiveAccount = "activeAccount"
        const val employeeEmployed = "employed"
        const val employeeEmploeeRole = "EmploeeRole"
        const val employeeEmpId = "empId"
        const val employeeRoleId = "roleId"
    }
}