package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dao.PaperTypeDAO
import pl.macia.printinghouse.server.dto.PaperType as PPaperType
import pl.macia.printinghouse.server.bmodel.PaperType
import pl.macia.printinghouse.server.bmodel.PaperTypeImpl


@Repository
internal class PaperTypeRepoImpl : PaperTypeIntRepo {
    @Autowired
    lateinit var dao: PaperTypeDAO
    private fun PPaperType.toBiz(): PaperType {
        return PaperTypeImpl(this)
    }

    override fun save(obj: PaperType): PaperType {
        obj as PaperTypeImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): PaperType? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findAll(): List<PaperType> {
        return dao.findAll().map { it.toBiz() }
    }
}