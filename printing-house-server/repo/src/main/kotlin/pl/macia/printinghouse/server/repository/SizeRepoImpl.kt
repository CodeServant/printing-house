package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Size
import pl.macia.printinghouse.server.bmodel.SizeImpl
import pl.macia.printinghouse.server.dto.Size as PSize
import pl.macia.printinghouse.server.dao.SizeDAO

@Repository
internal class SizeRepoImpl : SizeIntRepo {
    @Autowired
    lateinit var dao: SizeDAO
    private fun PSize.toBiz(): Size {
        return SizeImpl(this)
    }

    override fun save(obj: Size): Size {
        obj as SizeImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Size? {
        return dao.findByIdOrNull(id)?.toBiz()
    }
}