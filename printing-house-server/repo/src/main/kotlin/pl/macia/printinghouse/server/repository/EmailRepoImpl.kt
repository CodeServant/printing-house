package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.EmailImpl
import pl.macia.printinghouse.server.dao.EmailDAO

@Repository
class EmailRepoImpl : EmailRepo {
    @Autowired
    private lateinit var dao: EmailDAO

    override fun findById(id: Int): Email? {
        return dao.findByIdOrNull(id)?.let { EmailImpl(it) }
    }

    override fun save(email: Email): Email {
        email as EmailImpl
        return EmailImpl(dao.save(email.persistent))
    }
}