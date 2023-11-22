package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.ImpositionType
import pl.macia.printinghouse.server.bmodel.ImpositionTypeImpl
import pl.macia.printinghouse.server.dao.ImpositionTypeDAO
import pl.macia.printinghouse.server.dto.ImpositionType as PImpositionType

@Repository
internal class ImpositionTypeRepoImpl : ImpositionTypeIntRepo {
    @Autowired
    lateinit var dao: ImpositionTypeDAO
    private fun PImpositionType.toBiz(): ImpositionTypeImpl {
        return ImpositionTypeImpl(this)
    }

    override fun findById(id: Int): ImpositionType? {
        return dao.findByIdOrNull(id)?.let { ImpositionTypeImpl(it) }
    }

    override fun save(obj: ImpositionType): ImpositionType {
        return when (obj) {
            is ImpositionTypeImpl -> ImpositionTypeImpl(dao.save(obj.persistent))
        }
    }

    override fun findByName(name: String): ImpositionType? {
        return dao.findByName(name)?.toBiz()
    }

    override fun findAll(): List<ImpositionType> {
        return dao.findAll().map { it.toBiz() }
    }
}