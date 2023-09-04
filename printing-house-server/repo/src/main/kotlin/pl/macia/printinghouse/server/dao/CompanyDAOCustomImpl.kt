package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Company

@Repository
internal class CompanyDAOCustomImpl : CompanyDAOCustom {
    @Autowired
    lateinit var em: EntityManager
    override fun findClientById(id: Int): Company? {
        val query =
            em.createQuery("FROM Company as c WHERE c.client.id=:cliId", Company::class.java)
        query.setParameter("cliId", id)
        return query.resultList.firstOrNull()
    }
}