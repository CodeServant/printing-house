package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.server.bmodel.Bindery
import pl.macia.printinghouse.server.repository.BinderyRepo

@Service
class BinderyService {
    @Autowired
    private lateinit var repo: BinderyRepo
    fun listAllBinderies(): List<BinderyResp> {
        return repo.findAll().map {
            it.toTransport()
        }
    }

    fun findById(id: Int): BinderyResp? {
        return repo.findById(id)?.toTransport()
    }
}

private fun Bindery.toTransport(): BinderyResp {
    return BinderyResp(
        id = binderyId ?: throw ConversionException(),
        name = name
    )
}