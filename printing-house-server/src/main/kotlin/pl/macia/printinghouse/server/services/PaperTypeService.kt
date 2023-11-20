package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.PaperTypeResp
import pl.macia.printinghouse.server.bmodel.PaperType
import pl.macia.printinghouse.server.repository.PaperTypeRepo

@Service
class PaperTypeService {

    @Autowired
    private lateinit var repo: PaperTypeRepo
    fun findById(id: Int): PaperTypeResp? {
        return try {
            repo.findById(id)?.toTransport()
        } catch (e: ConversionException) {
            null
        }
    }
}

private fun PaperType.toTransport(): PaperTypeResp {
    return PaperTypeResp(
        papTypeId
            ?: throw ConversionException("${PaperType::class.simpleName} can't be converted the ${PaperType::papTypeId.name} is null"),
        name,
        shortName
    )
}