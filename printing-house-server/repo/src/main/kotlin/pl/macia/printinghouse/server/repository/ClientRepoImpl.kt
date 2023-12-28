package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Client
import pl.macia.printinghouse.server.bmodel.ClientImpl
import pl.macia.printinghouse.server.dao.ClientDAO

@Repository
internal class ClientRepoImpl : ClientIntRepo {
    @Autowired
    lateinit var dao: ClientDAO

    @Autowired
    private lateinit var indCliRepo: IndividualClientRepo

    @Autowired
    private lateinit var companyClientRepo: CompanyClientRepo
    override fun findById(id: Int): Client? {
        return dao.findByIdOrNull(id)?.let(::ClientImpl)
    }

    override fun findTypedById(id: Int): Client? {
        val possibilities = listOfNotNull(
            indCliRepo.findByClientId(id),
            companyClientRepo.findByClientId(id)
        )
        return possibilities.firstOrNull()
    }

}