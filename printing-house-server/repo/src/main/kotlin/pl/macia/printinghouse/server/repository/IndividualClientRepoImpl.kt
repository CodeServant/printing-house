package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.IndividualClient
import pl.macia.printinghouse.server.bmodel.IndividualClientImpl
import pl.macia.printinghouse.server.dao.IndividualClientDAO

@Repository
internal class IndividualClientRepoImpl : IndividualClientRepo {
    @Autowired
    private lateinit var dao: IndividualClientDAO
    override fun findById(id: Int): IndividualClientImpl? {
        return dao.findByIdOrNull(id)?.let { IndividualClientImpl(it) }

    }

    override fun save(indCLi: IndividualClient): IndividualClientImpl {
        indCLi as IndividualClientImpl
        return IndividualClientImpl(dao.save(indCLi.persistent))
    }
}