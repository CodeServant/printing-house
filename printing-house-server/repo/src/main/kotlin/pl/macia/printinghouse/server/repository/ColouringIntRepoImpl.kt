package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Colouring
import pl.macia.printinghouse.server.bmodel.ColouringImpl
import pl.macia.printinghouse.server.dao.ColouringDAO

@Repository
internal class ColouringRepoImpl : ColouringIntRepo {
    @Autowired
    lateinit var dao: ColouringDAO
    override fun findById(id: Byte): Colouring? {
        return dao.findByIdOrNull(id)?.let { ColouringImpl(it) }
    }

    override fun save(obj: Colouring): Colouring {
        obj as ColouringImpl
        return ColouringImpl(dao.save(obj.persistent))
    }
}