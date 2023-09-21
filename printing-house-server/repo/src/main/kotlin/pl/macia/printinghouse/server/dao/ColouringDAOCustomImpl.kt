package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Colouring

@Repository
internal class ColouringDAOCustomImpl : ColouringDAOCustom {
    @Autowired
    lateinit var em: EntityManager
    override fun findByPalette(firstSide: Byte, secondSide: Byte): Colouring? {
        val query =
            em.createQuery(
                "FROM Colouring as c WHERE c.firstSide=:first and c.secondSide=:second",
                Colouring::class.java
            )
        query.setParameter("first", firstSide)
        query.setParameter("second", secondSide)
        return query.resultList.firstOrNull()
    }

}