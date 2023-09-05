package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Client

@Repository
internal class ClientIntRepoImpl : ClientIntRepo {
    @Autowired
    lateinit var dao: List<ClientRepos>
    override fun findById(id: Int): Client? {
        dao.forEach {
            it.findByClientId(id)?.let {
                return it
            }
        }
        return null
    }

}