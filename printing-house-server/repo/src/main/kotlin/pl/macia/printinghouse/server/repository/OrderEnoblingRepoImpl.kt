package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.OrderEnobling
import pl.macia.printinghouse.server.bmodel.OrderEnoblingImpl
import pl.macia.printinghouse.server.dao.OrderEnoblingDAO
import pl.macia.printinghouse.server.dto.OrderEnobling as POrderEnobling

@Repository
internal class OrderEnoblingRepoImpl : OrderEnoblingIntRepo {

    @Autowired
    lateinit var dao: OrderEnoblingDAO
    fun POrderEnobling.toBiz(): OrderEnobling {
        return OrderEnoblingImpl(this)
    }

    override fun save(obj: OrderEnobling): OrderEnobling {
        obj as OrderEnoblingImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): OrderEnobling? {
        return dao.findByIdOrNull(id)?.toBiz()
    }
}