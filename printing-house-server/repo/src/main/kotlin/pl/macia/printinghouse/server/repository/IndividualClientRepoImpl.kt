package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Client
import pl.macia.printinghouse.server.bmodel.IndividualClient
import pl.macia.printinghouse.server.bmodel.IndividualClientImpl
import pl.macia.printinghouse.server.dao.IndividualClientDAO

@Repository
internal class IndividualClientRepoImpl : IndividualClientIntRepo {
    @Autowired
    private lateinit var dao: IndividualClientDAO
    override fun findByClientId(clientId: Int): IndividualClient? {
        return dao.findByClientId(clientId)?.let { IndividualClientImpl(it) }
    }

    override fun findByPersonId(personId: Int): IndividualClient? {
        return dao.findByIdOrNull(personId)?.let { IndividualClientImpl(it) }
    }

    override fun save(obj: IndividualClient): IndividualClientImpl {
        obj as IndividualClientImpl
        return IndividualClientImpl(dao.save(obj.persistent))
    }

    override fun Client.isIndividualClient(): Boolean {
        if (this is IndividualClient) return true
        val id = clientId ?: return false
        return findByClientId(id) != null
    }
}