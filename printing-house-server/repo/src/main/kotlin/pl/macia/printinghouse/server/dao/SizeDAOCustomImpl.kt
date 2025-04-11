package pl.macia.printinghouse.server.dao

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
internal class SizeDAOCustomImpl : SizeDAOCustom {
    @Autowired
    lateinit var em: EntityManager
}