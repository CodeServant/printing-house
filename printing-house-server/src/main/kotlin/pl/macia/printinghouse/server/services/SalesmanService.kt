package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.SalesmanChangeReq
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

    @Transactional
    fun change(id: Int, salesmanChange: SalesmanChangeReq): Boolean {

        var salesmanChanged = false
        val found = repo.findById(id) ?: return false


        //todo this code is almost the same as code in worker and should be refactored f.e. create employee change request
        fun <E> simpleChange(workerChange: E, found: E, setFound: (E) -> Unit) {
            workerChange?.let {
                if (found != it) {
                    setFound(it)
                    salesmanChanged = true
                }
            }
        }
        simpleChange(salesmanChange.employed, found.employed) { found.employed = it!! }
        simpleChange(salesmanChange.activeAccount, found.activeAccount) { found.activeAccount = it!! }
        salesmanChange.password?.let {
            //password changes allways if not null because of salt
            found.password = passwordEncoder.encode(it)
            salesmanChanged = true
        }
        simpleChange(salesmanChange.email, found.email.email) { found.email.email = it!! }
        simpleChange(salesmanChange.psudoPESEL, found.psudoPESEL) { found.psudoPESEL = it!! }
        simpleChange(salesmanChange.surname, found.surname) { found.surname = it!! }
        simpleChange(salesmanChange.name, found.name) { found.name = it!! }
        return salesmanChanged
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