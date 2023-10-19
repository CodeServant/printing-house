package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.response.RecID
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

    /**
     * @throws ConversionException
     */
    @Transactional
    fun insertNew(req: BinderyReq): RecID {
        val saved = repo.save(Bindery(req.name))
        val s:Int = saved.binderyId ?: throw ConversionException()
        return RecID(s.toLong())
    }
}

/**
 * @throws ConversionException
 */
private fun Bindery.toTransport(): BinderyResp {
    return BinderyResp(
        id = binderyId ?: throw ConversionException(),
        name = name
    )
}