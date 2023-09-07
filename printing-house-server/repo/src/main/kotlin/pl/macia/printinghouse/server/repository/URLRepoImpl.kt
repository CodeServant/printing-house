package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.URL
import pl.macia.printinghouse.server.bmodel.URLImpl
import pl.macia.printinghouse.server.dao.UrlDAO
import pl.macia.printinghouse.server.dto.URL as PURL

@Repository
internal class URLRepoImpl : URLIntRepo {
    @Autowired
    lateinit var dao: UrlDAO
    private fun PURL.toBiz(): URL {
        return URLImpl(this)
    }

    override fun findById(id: Long): URL? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun save(obj: URL): URL {
        obj as URLImpl
        return dao.save(obj.persistent).toBiz()
    }
}