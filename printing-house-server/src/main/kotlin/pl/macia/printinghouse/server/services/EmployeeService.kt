package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.macia.printinghouse.request.EmployeeChangeReq
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.bmodel.Employee
import pl.macia.printinghouse.server.repository.RoleRepo

@Service
class EmployeeService {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var roleRepo: RoleRepo

    /**
     * Passes the [Employee] object to further process its state for the employees objects when just inserted.
     */
    internal fun justInserted(emp: Employee) {
        emp.roleAsEmployee()
    }

    /**
     * Check change request in generic employee part of data and changes those that should be changed.
     * Note that this updates the state of [employee] object.
     * @return true if change was made, false if not
     */
    internal fun change(employee: Employee, emplChange: EmployeeChangeReq): Boolean {
        var empChanged = false

        fun <E> simpleChange(empChange: E, found: E, setFound: (E) -> Unit) {
            empChange?.let {
                if (found != it) {
                    setFound(it)
                    empChanged = true
                }
            }
        }
        simpleChange(emplChange.employed, employee.employed) { employee.employed = it!! }
        simpleChange(emplChange.activeAccount, employee.activeAccount) { employee.activeAccount = it!! }
        emplChange.password?.let {
            //password changes allways if not null because of salt
            employee.password = passwordEncoder.encode(it)
            empChanged = true
        }
        simpleChange(emplChange.email, employee.email.email) { employee.email.email = it!! }
        simpleChange(emplChange.psudoPESEL, employee.psudoPESEL) { employee.psudoPESEL = it!! }
        simpleChange(emplChange.surname, employee.surname) { employee.surname = it!! }
        simpleChange(emplChange.name, employee.name) { employee.name = it!! }
        employee.roleAsEmployee()
        return empChanged
    }

    /**
     * Sets this objects role to [PrimaryRoles.EMPLOYEE] role.
     */
    private fun Employee.roleAsEmployee() {
        this.roles.add(roleRepo.mergeName(PrimaryRoles.EMPLOYEE))
    }
}