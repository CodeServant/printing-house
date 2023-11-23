package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.response.GeneralEnoblingResp
import pl.macia.printinghouse.response.PunchResp
import pl.macia.printinghouse.response.UVVarnishResp
import pl.macia.printinghouse.server.bmodel.Enobling
import pl.macia.printinghouse.server.bmodel.Punch
import pl.macia.printinghouse.server.bmodel.UVVarnish
import pl.macia.printinghouse.server.repository.EnoblingRepo

@Service
class EnoblingService {
    @Autowired
    private lateinit var repo: EnoblingRepo

    fun findById(id: Int): EnoblingResp? {
        return repo.findByIdTyped(id)?.toTransport()
    }

    fun allEnoblings(): List<EnoblingResp> {
        return repo.findAllTyped().map { it.toTransport() }
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