package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Worker

@Repository
internal class WorkerDAOCustomImpl : WorkerDAOCustom {
    @Autowired
    lateinit var em: EntityManager
    override fun findByEmail(email: String): Worker? {
        val query = em.createQuery("from Worker w where w.email.email=:eml", Worker::class.java)
        query.setParameter("eml", email)
        return query.resultList.firstOrNull()
    }
}