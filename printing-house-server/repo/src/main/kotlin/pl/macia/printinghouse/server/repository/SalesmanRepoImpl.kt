package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Salesman
import pl.macia.printinghouse.server.bmodel.SalesmanImpl
import pl.macia.printinghouse.server.dao.SalesmanDAO
import pl.macia.printinghouse.server.dto.Salesman as PSalesman

@Repository
internal class SalesmanRepoImpl : SalesmanIntRepo {
    @Autowired
    lateinit var dao: SalesmanDAO
    private fun PSalesman.toBiz(): SalesmanImpl = SalesmanImpl(this)
    override fun save(obj: Salesman): Salesman {
        obj as SalesmanImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Salesman? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findAllHired(): List<Salesman> {
        return dao.findByEmployed(true).map { it.toBiz() }
    }

    override fun deleteById(id: Int) {
        dao.deleteById(id)
    }

    override fun findByEmail(email: String): Salesman? {
        return dao.findByEmail(email)?.toBiz()
    }
}