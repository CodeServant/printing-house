package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.*
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.ClientRepo
import pl.macia.printinghouse.server.repository.CompanyClientRepo
import pl.macia.printinghouse.server.repository.IndividualClientRepo

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

    @Transactional
    fun createNew(newClient: ClientReq): RecID {
        val clientId: Int
        // todo add individual client of existing person
        when (newClient) {
            is CompanyClientReq -> {
                clientId = compCliRepo.save(
                    CompanyClient(
                        name = newClient.name,
                        nip = newClient.nip,
                        email = newClient.email?.let { Email(it) },
                        phoneNumber = newClient.phoneNumber
                    )
                ).clientId!!
            }

            is IndividualClientReq -> {
                clientId = indCliRepo.save(
                    IndividualClient(
                        email = newClient.email?.let { Email(it) },
                        phoneNumber = newClient.phoneNumber,
                        psudoPESEL = newClient.psudoPESEL,
                        surname = newClient.surname,
                        name = newClient.name
                    )
                ).clientId!!
            }
        }
        return RecID(clientId.toLong())
    }

    @Transactional
    fun changeOne(id: Int, clientChange: ClientChangeReq): ChangeResp? {
        var changed = false

        /**
         * Simply change [found] nullable value for [cliChange] optionally. If [ClientChangeReq.nullingRest] is enabled then [found] can
         * be set to null if [cliChange] is null.
         */
        fun <E> simpleChangeNullable(cliChange: E?, found: E?, setNull: () -> Unit, setFound: (E) -> Unit) {
            if (found == cliChange) return
            if (cliChange != null) {
                setFound(cliChange)
                changed = true
            } else if (clientChange.nullingRest) {
                setNull()
                changed = true
            }
        }

        /**
         * Simply change [found] value for [cliChange] if the latter is not null.
         */
        fun <E> simpleChangeNonNullable(cliChange: E?, found: E, setFound: (E) -> Unit) {
            if (cliChange != null && found != cliChange) {
                setFound(cliChange)
                changed = true
            }
        }

        val found: Client = when (clientChange) {
            is CompanyClientChangeReq -> compCliRepo.findByClientId(id)
            is IndividualClientChangeReq -> indCliRepo.findByClientId(id)
        } ?: return null


        simpleChangeNullable( /* todo currently impossible to set email that already exists as email of other type of person
                                  possible sollution is to create method in repo that gets the email or creates new if not existing */
            clientChange.email,
            found.email?.email,
            { found.email = null },
            {
                if (found.email != null)
                    found.email?.email = it
                else
                    found.email = Email(it)
            }
        )
        simpleChangeNullable(
            clientChange.phoneNumber,
            found.phoneNumber,
            { found.phoneNumber = null },
            { found.phoneNumber = it }
        )

        when (clientChange) {
            is CompanyClientChangeReq -> {
                found as CompanyClient
                simpleChangeNonNullable(clientChange.name, found.name, found::name::set)
                simpleChangeNonNullable(clientChange.nip, found.nip, found::nip::set)
            }

            is IndividualClientChangeReq -> {
                found as IndividualClient
                simpleChangeNonNullable(clientChange.name, found.name, found::name::set)
                simpleChangeNonNullable(clientChange.surname, found.surname, found::surname::set)
                simpleChangeNonNullable(clientChange.psudoPESEL, found.psudoPESEL, found::psudoPESEL::set)
                simpleChangeNonNullable(clientChange.name, found.name, found::name::set)
            }
        }
        return ChangeResp(changed)
    }

    fun queryClient(query: String): Collection<ClientResp> {
        val foundCompanies = compCliRepo.searchByName(query).map {
            it.toTransport()
        }
        val foundIndividuals = indCliRepo.searchQuery(query).map {
            it.toTransport()
        }
        return listOf(foundCompanies, foundIndividuals).flatten()
    }
}

/**
 * Converts [Client] to [ClientResp]. "Note that you can only
 * convert instances of [Client] and not [Client] itself."
 * @throws ConversionException
 */
internal fun Client.toTransport(repo: ClientRepo): ClientResp {
    return when (this) {
        is CompanyClient -> this.toTransport()
        is IndividualClient -> this.toTransport()
        else -> {
            val typed = repo.findTypedById(
                this.clientId ?: throw ConversionException("${this::clientId.name} not provided")
            )
            typed?.toTransport(repo)
                ?: throw ConversionException(
                    """can't convert ${Client::class.qualifiedName} to any 
                    |subtype of ${ClientResp::class.qualifiedName}""".trimMargin()
                )
        }
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