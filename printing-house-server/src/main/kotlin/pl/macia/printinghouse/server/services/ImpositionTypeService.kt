package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.ImpositionTypeResp
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
}

private fun ImpositionType.toTransport(): ImpositionTypeResp {
    return ImpositionTypeResp(
        impTypId
            ?: throw ConversionException("${ImpositionType::class.qualifiedName} does not have ${ImpositionType::impTypId.name} defined"),
        name
    )
}