package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.*
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.EnoblingRepo
import pl.macia.printinghouse.server.repository.PunchRepo
import pl.macia.printinghouse.server.repository.UVVarnishRepo

@Service
class EnoblingService {
    @Autowired
    private lateinit var repo: EnoblingRepo

    @Autowired
    private lateinit var repoUVVarnish: UVVarnishRepo

    @Autowired
    private lateinit var repoPunch: PunchRepo
    fun findById(id: Int): EnoblingResp? {
        return repo.findByIdTyped(id)?.toTransport()
    }

    fun allEnoblings(): List<EnoblingResp> {
        return repo.findAllTyped().map { it.toTransport() }
    }

    @Transactional
    fun insertNew(req: IEnoblingReq): RecID {
        val enobling = when (req) {
            is EnoblingReq -> repo.save(Enobling(req.name, req.description))
            is PunchReq -> repoPunch.save(Punch(req.name, req.description))
            is UVVarnishReq -> repoUVVarnish.save(UVVarnish(req.name, req.description))
        }
        return RecID(
            enobling.enoblingId?.toLong()
                ?: throw ConversionException("${Enobling::class.qualifiedName} with ${Enobling::enoblingId.name} not saved properly")
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Transactional
    fun changeEnobling(id: Int, changeReq: IEnoblingChangeReq): ChangeResp? {
        val enobling = repo.findByIdTyped(id) ?: return null
        val changeTracker = ChangeTracker(changeReq.nullingRest)

        val typesMatch = when (enobling) {
            is Punch -> PunchChangeReq.serializer().descriptor.serialName == Punch::class.simpleName
            is UVVarnish -> UVVarnishChangeReq.serializer().descriptor.serialName == UVVarnish::class.simpleName
            is Enobling -> EnoblingChangeReq.serializer().descriptor.serialName == Enobling::class.simpleName
        }
        //if the user requested change for different enobling type that the found one
        if (!typesMatch) return null

        changeTracker.applyChange(changeReq.name, enobling::name)
        changeTracker.applyChange(changeReq.description, enobling::description)

        repo.saveTyped(enobling)

        return ChangeResp(changeTracker.changed)
    }
}

private fun Enobling.toTransport(): EnoblingResp {
    val c = when (this) {
        is Punch -> ::PunchResp
        is UVVarnish -> ::UVVarnishResp
        else -> ::GeneralEnoblingResp
    }
    return c(
        enoblingId ?: throw ConversionException("${Enobling::enoblingId.name} is null so converion is impossible"),
        name,
        description
    )
}