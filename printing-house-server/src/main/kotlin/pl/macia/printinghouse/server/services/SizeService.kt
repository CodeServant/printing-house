package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.response.SizeResp
import pl.macia.printinghouse.server.bmodel.Size
import pl.macia.printinghouse.server.repository.SizeRepo

@Service
class SizeService {

    @Autowired
    private lateinit var repo: SizeRepo
    fun allSizes(): List<SizeResp> {
        return repo.allNamedSizes().map {
            it.toResp()
        }
    }
}

private fun Size.toResp(): SizeResp {
    return SizeResp(
        id = sizeId ?: throw RuntimeException("${Size::sizeId.name} not provided"),
        name = name,
        width = width,
        heigth = heigth
    )
}