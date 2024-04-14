package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Salesman

@Repository
internal class SalesmanDAOCustomImpl : SalesmanDAOCustom {
    @Autowired
    lateinit var em: EntityManager
    override fun findByEmail(email: String): Salesman? {
        val query =
            em.createQuery("FROM Salesman as e WHERE e.email.email=:email", Salesman::class.java)
        query.setParameter("email", email)
        return query.resultList.firstOrNull()
    }
}