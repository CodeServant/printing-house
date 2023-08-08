package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Bindery

@Repository
class BinderyDAOImpl @Autowired constructor(
    private val entityManager: EntityManager
) : BinderyDAO {
    @Transactional
    override fun findById(id: Int): Bindery? {
        return entityManager.find(Bindery::class.java, id)
    }

    @Transactional
    override fun findByName(name: String): Bindery? {
        val param = "name"
        val col = param
        val binQ = entityManager.createQuery("FROM Bindery WHERE $col=:$param", Bindery::class.java)
        binQ.setParameter(param, name)
        return binQ.singleResult
    }

    @Transactional
    override fun create(bindery: Bindery) {
        entityManager.persist(bindery)
    }

    /**
     * Keep in mind that this method if entity is detached don't check actual equality.
     */
    @Transactional
    override fun delete(bindery: Bindery){
        if (entityManager.contains(bindery)) {
            entityManager.remove(bindery)
        } else {
            val ee: Bindery = entityManager.getReference(bindery::class.java, bindery.id)
            entityManager.remove(ee)
        }
    }
}