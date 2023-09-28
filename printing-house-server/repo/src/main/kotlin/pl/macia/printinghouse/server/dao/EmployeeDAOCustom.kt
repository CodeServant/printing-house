package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Employee

internal interface EmployeeDAOCustom {
    fun findByEmail(email: String): Employee?
}