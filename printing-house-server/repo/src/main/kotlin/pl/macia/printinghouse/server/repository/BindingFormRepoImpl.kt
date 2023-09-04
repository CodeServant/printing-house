package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.BindingForm
import pl.macia.printinghouse.server.bmodel.BindingFormImpl
import pl.macia.printinghouse.server.dao.BindingFormDAO

@Repository
internal class BindingFormRepoImpl : BindingFormIntRepo {
    @Autowired
    lateinit var dao: BindingFormDAO
    override fun save(obj: BindingForm): BindingForm {
        obj as BindingFormImpl
        return BindingFormImpl(dao.save(obj.persistent))
    }

    override fun findById(id: Int): BindingForm? {
        return dao.findByIdOrNull(id)?.let { BindingFormImpl(it) }
    }
}