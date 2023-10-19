package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.BindingFormResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.server.bmodel.BindingForm
import pl.macia.printinghouse.server.repository.BindingFormRepo

@Service
class BindingFormService {
    @Autowired
    private lateinit var repo: BindingFormRepo
    fun listAllBindingForms(): List<BindingFormResp> {
        return repo.findAll().map {
            it.toTransport()
        }
    }

    fun findById(id: Int): BindingFormResp? {
        return repo.findById(id)?.toTransport()
    }

    @Transactional
    fun insertNew(newBindForm: BindingFormReq): RecID {
        val insertedId = repo.save(
            BindingForm(newBindForm.name)
        ).bindingFormId!!
        return RecID(insertedId.toLong())
    }
}

private fun BindingForm.toTransport(): BindingFormResp {
    return BindingFormResp(
        id = bindingFormId ?: throw ConversionException(),
        name = name
    )
}