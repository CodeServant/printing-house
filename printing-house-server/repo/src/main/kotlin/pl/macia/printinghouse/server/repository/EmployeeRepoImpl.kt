package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Employee
import pl.macia.printinghouse.server.bmodel.EmployeeImpl
import pl.macia.printinghouse.server.dao.EmployeeDAO
import pl.macia.printinghouse.server.dto.Employee as PEmployee

@Repository
internal class EmployeeRepoImpl : EmployeeIntRepo {
    @Autowired
    lateinit var dao: EmployeeDAO
    private fun PEmployee.toBiz(): EmployeeImpl {
        return EmployeeImpl(this)
    }

    override fun findByEmail(email: String): Employee? {
        return dao.findByEmail(email)?.toBiz()
    }
}