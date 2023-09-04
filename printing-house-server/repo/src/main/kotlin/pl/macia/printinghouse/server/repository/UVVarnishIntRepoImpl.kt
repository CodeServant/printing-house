package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.UVVarnish
import pl.macia.printinghouse.server.bmodel.UVVarnishImpl
import pl.macia.printinghouse.server.dao.UVVarnishDAO

@Repository
internal class UVVarnishIntRepoImpl : UVVarnishIntRepo {
    @Autowired
    lateinit var dao: UVVarnishDAO
    override fun findById(id: Int): UVVarnish? {
        return dao.findByIdOrNull(id)?.let { UVVarnishImpl(it) }
    }

    override fun save(obj: UVVarnish): UVVarnish {
        obj as UVVarnishImpl
        return UVVarnishImpl(dao.save(obj.persistent))
    }
}