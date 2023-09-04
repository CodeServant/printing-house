package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.CompanyClient
import pl.macia.printinghouse.server.bmodel.CompanyClientImpl
import pl.macia.printinghouse.server.dao.CompanyDAO

@Repository
internal class CompanyClientRepoImpl : CompanyClientIntRepo {
    @Autowired
    lateinit var dao: CompanyDAO
    override fun findByCompanyId(companyId: Int): CompanyClient? {
        return dao.findByIdOrNull(companyId)?.let { CompanyClientImpl(it) }
    }

    override fun findByClientId(clientId: Int): CompanyClient? {
        return dao.findClientById(clientId)?.let { CompanyClientImpl(it) }
    }

    override fun save(obj: CompanyClient): CompanyClient {
        obj as CompanyClientImpl
        return CompanyClientImpl(
            dao.save(obj.persistent)
        )
    }
}