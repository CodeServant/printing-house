package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.EnoblingReq
import pl.macia.printinghouse.request.IEnoblingReq
import pl.macia.printinghouse.request.PunchReq
import pl.macia.printinghouse.request.UVVarnishReq
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.Enobling
import pl.macia.printinghouse.server.bmodel.Punch
import pl.macia.printinghouse.server.bmodel.UVVarnish
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