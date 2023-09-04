package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Bindery
import pl.macia.printinghouse.server.bmodel.BinderyImpl
import pl.macia.printinghouse.server.dao.BinderyDAO

@Repository
internal class BinderyRepoImpl : BinderyIntRepo {
    @Autowired
    lateinit var dao: BinderyDAO
    override fun findById(id: Int): Bindery? = dao.findByIdOrNull(id)?.let { BinderyImpl(it) }

    override fun save(obj: Bindery): Bindery {
        obj as BinderyImpl
        return BinderyImpl(dao.save(obj.persistent))
    }
}