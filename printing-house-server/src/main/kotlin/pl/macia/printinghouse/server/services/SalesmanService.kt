package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.SalesmanResp
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.SalesmanRepo

@Service
class SalesmanService {
    @Autowired
    private lateinit var repo: SalesmanRepo

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    fun listSalesmans(): List<SalesmanResp> {
        return repo.findAllHired().map { it.toTransport() }
    }

    fun findById(id: Int): SalesmanResp? {
        return repo.findById(id)?.toTransport()
    }

    @Transactional
    fun insertNew(salesman: SalesmanReq): RecID {
        val inserted = repo.save(
            Salesman(
                email = Email(salesman.email),
                password = passwordEncoder.encode(salesman.password),
                activeAccount = salesman.activeAccount,
                employed = salesman.employed,
                name = salesman.name,
                surname = salesman.surname,
                pseudoPESEL = salesman.psudoPESEL
            )
        )
        return RecID(inserted.personId!!.toLong())
    }

    @Transactional
    fun delete(id: RecID) {
        repo.deleteById(id.asInt())
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