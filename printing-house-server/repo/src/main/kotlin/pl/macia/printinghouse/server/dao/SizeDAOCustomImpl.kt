package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Size

@Repository
internal class SizeDAOCustomImpl : SizeDAOCustom {
    @Autowired
    lateinit var em: EntityManager

    @Transactional
    override fun findOrCreate(width: Double, height: Double): Size {
        val query = em.createQuery("from Size s where s.width=:width and s.heigth=:height", Size::class.java)
        query.setParameter("width", width)
        query.setParameter("height", height)
        val size = query.resultList.firstOrNull()
        return size ?: Size(width, height)
    }
}