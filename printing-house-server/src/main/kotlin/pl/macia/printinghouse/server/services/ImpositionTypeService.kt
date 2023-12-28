package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.ImpositionTypeChangeReq
import pl.macia.printinghouse.request.ImpositionTypeReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.ImpositionTypeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.server.bmodel.ImpositionType
import pl.macia.printinghouse.server.repository.ImpositionTypeRepo

@Service
class ImpositionTypeService {
    @Autowired
    private lateinit var repo: ImpositionTypeRepo
    fun findById(id: Int): ImpositionTypeResp? {
        val found = repo.findById(id) ?: return null
        return found.toTransport()
    }

    fun allImpositionTypes(): List<ImpositionTypeResp> {
        return repo.findAll().map { it.toTransport() }
    }

    @Transactional
    fun insertNew(req: ImpositionTypeReq): RecID {
        val id = repo.save(ImpositionType(req.name)).impTypId
            ?: throw Exception("some error while saving ${ImpositionType::class.simpleName} to database")
        return RecID(id.toLong())
    }

    @Transactional
    fun changeImpositionType(id: Int, changeReq: ImpositionTypeChangeReq): ChangeResp? {
        val found = repo.findById(id) ?: return null
        val changeTracker = ChangeTracker()
        changeTracker.apply {
            applyChange(changeReq.name, found::name)
        }
        return ChangeResp(changeTracker.changed)
    }
}

/**
 * @throws ConversionException
 */
internal fun ImpositionType.toTransport(): ImpositionTypeResp {
    return ImpositionTypeResp(
        impTypId
            ?: throw ConversionException("${ImpositionType::class.qualifiedName} does not have ${ImpositionType::impTypId.name} defined"),
        name
    )
}