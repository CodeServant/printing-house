package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

const val tableEmployee = "Employee"
const val employeeId = "personId"
const val employeeEmail = "email"
const val employeePassword = "password"
const val employeeActiveAccount = "activeAccount"
const val employeeEmployed = "employed"

@Poko
@Entity
@Table(name = tableEmployee)
@PrimaryKeyJoinColumn(name = employeeId)
@Inheritance(strategy = InheritanceType.JOINED)
class Employee(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = employeeEmail, referencedColumnName = emailId)
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
) : Person(name, surname, pseudoPESEL)