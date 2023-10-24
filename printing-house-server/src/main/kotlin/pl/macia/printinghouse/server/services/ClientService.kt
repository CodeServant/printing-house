package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.response.CompanyClientResp
import pl.macia.printinghouse.response.IndividualClientResp
import pl.macia.printinghouse.server.bmodel.CompanyClient
import pl.macia.printinghouse.server.bmodel.IndividualClient
import pl.macia.printinghouse.server.repository.CompanyClientRepo
import pl.macia.printinghouse.server.repository.IndividualClientRepo
import pl.macia.printinghouse.server.bmodel.Client

@Service
class ClientService {
    @Autowired
    private lateinit var indCliRepo: IndividualClientRepo

    @Autowired
    private lateinit var compCliRepo: CompanyClientRepo

    /**
     * Check all kinds of [Client] repositories for existence of one with provided [id].
     */
    @Transactional
    fun findById(id: Int): ClientResp? {
        val compCli = compCliRepo.findByClientId(id)
        val indCli = indCliRepo.findByClientId(id)
        val clientsGroups = mutableListOf(compCli?.toTransport(), indCli?.toTransport())
        clientsGroups.find {
            it != null
        }?.run {
            return this
        }
        return null
    }
}

/**
 * @throws ConversionException
 */
private fun CompanyClient.toTransport(): CompanyClientResp {
    return CompanyClientResp(
        clientId ?: throw ConversionException(),
        phoneNumber,
        email?.email,
        companyId ?: throw ConversionException(),
        nip,
        name
    )
}

/**
 * @throws ConversionException
 */
private fun IndividualClient.toTransport(): IndividualClientResp {
    return IndividualClientResp(
        clientId ?: throw ConversionException(),
        phoneNumber,
        email?.email,
        personId ?: throw ConversionException(),
        psudoPESEL,
        surname,
        name
    )
}