package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Employee

internal interface EmployeeDAO: JpaRepository<Employee, Int>, EmployeeDAOCustom