package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Employee

@Repository
internal class EmployeeDAOCustomImpl : EmployeeDAOCustom {
    @Autowired
    lateinit var em: EntityManager
    override fun findByEmail(email: String): Employee? {
        val query =
            em.createQuery("FROM Employee as e WHERE e.email.email=:email", Employee::class.java)
        query.setParameter("email", email)
        return query.resultList.firstOrNull()
    }
}