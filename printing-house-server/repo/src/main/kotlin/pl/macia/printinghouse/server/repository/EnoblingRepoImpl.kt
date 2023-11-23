package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Enobling
import pl.macia.printinghouse.server.bmodel.EnoblingImpl
import pl.macia.printinghouse.server.dao.EnoblingDAO

@Repository
internal class EnoblingRepoImpl : EnoblingIntRepo {
    @Autowired
    lateinit var dao: EnoblingDAO

    @Autowired
    private lateinit var repoPunch: PunchRepo

    @Autowired
    private lateinit var repoUVVarnish: UVVarnishRepo


    override fun findById(id: Int): Enobling? {
        return dao.findByIdOrNull(id)?.let { EnoblingImpl(it) }
    }

    override fun save(obj: Enobling): Enobling {
        obj as EnoblingImpl
        return EnoblingImpl(dao.save(obj.persistent))
    }

    override fun findByIdTyped(id: Int): Enobling? {
        val en = findById(id) ?: return null
        sequenceOf(
            repoUVVarnish.findById(id),
            repoPunch.findById(id)
        ).firstOrNull { it != null }
            ?.apply { return this }
        return en
    }
}