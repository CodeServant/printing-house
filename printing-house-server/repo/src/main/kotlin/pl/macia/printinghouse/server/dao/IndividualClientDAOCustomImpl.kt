package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.IndividualClient

@Repository
internal class IndividualClientDAOCustomImpl : IndividualClientDAOCustom {
    @Autowired
    lateinit var em: EntityManager

    override fun findByEmail(email: String): IndividualClient? {
        val query =
            em.createQuery("FROM IndividualClient as ic WHERE ic.client.email.email=:eml", IndividualClient::class.java)
        query.setParameter("eml", email)
        return query.resultList.firstOrNull()
    }

    override fun searchQuery(stringQuery: String): List<IndividualClient> {
        val query =
            em.createQuery(
                "FROM IndividualClient as ic WHERE CONCAT(ic.name, ' ', ic.surname) LIKE :qry",
                IndividualClient::class.java
            )
        query.setParameter("qry", "$stringQuery%")
        return query.resultList
    }
}