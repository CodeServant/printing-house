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
    override fun findById(id: Int): Bindery {
        return entityManager.find(Bindery::class.java, id)
    }
}