package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.ImpositionType
import pl.macia.printinghouse.server.bmodel.ImpositionTypeImpl
import pl.macia.printinghouse.server.dao.ImpositionTypeDAO

@Repository
internal class ImpositionTypeRepoImpl : ImpositionTypeIntRepo {
    @Autowired
    lateinit var dao: ImpositionTypeDAO
    override fun findById(id: Int): ImpositionType? {
        return dao.findByIdOrNull(id)?.let { ImpositionTypeImpl(it) }
    }

    override fun save(obj: ImpositionType): ImpositionType {
        return when (obj) {
            is ImpositionTypeImpl -> ImpositionTypeImpl(dao.save(obj.persistent))
        }
    }
}