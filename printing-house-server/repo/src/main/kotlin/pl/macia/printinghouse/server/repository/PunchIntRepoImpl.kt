package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Punch
import pl.macia.printinghouse.server.bmodel.PunchImpl
import pl.macia.printinghouse.server.dao.PunchDAO

@Repository
internal class PunchIntRepoImpl : PunchIntRepo {
    @Autowired
    lateinit var dao: PunchDAO
    override fun findById(id: Int): Punch? {
        return dao.findByIdOrNull(id)?.let { PunchImpl(it) }
    }

    override fun save(obj: Punch): Punch {
        obj as PunchImpl
        return PunchImpl(dao.save(obj.persistent))
    }

    override fun findAll(): List<Punch> {
        return dao.findAll().map { PunchImpl(it) }
    }
}