package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.SalesmanResp
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.SalesmanRepo

@Service
class SalesmanService {
    @Autowired
    private lateinit var repo: SalesmanRepo
    fun listSalesmans(): List<SalesmanResp> {
        return repo.findAllHired().map { it.toTransport() }
    }

    fun findById(id: Int): SalesmanResp? {
        return repo.findById(id)?.toTransport()
    }

}

/**
 * @throws ConversionException
 */
private fun Salesman.toTransport(): SalesmanResp {
    return SalesmanResp(
        id = personId ?: throw ConversionException(),
        employed = employed,
        activeAccount = activeAccount,
        roles = roles.map {
            RoleResp(it.roleId ?: throw ConversionException(), it.name)
        },
        email = email.email,
        psudoPESEL = psudoPESEL,
        surname = surname,
        name = name
    )
}