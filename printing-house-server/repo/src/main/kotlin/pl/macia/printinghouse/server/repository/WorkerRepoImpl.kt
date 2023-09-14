package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.dto.Worker as PWorker
import pl.macia.printinghouse.server.bmodel.WorkerImpl
import pl.macia.printinghouse.server.dao.WorkerDAO

@Repository
internal class WorkerRepoImpl : WorkerIntRepo {
    @Autowired
    lateinit var dao: WorkerDAO
    private fun PWorker.toBiz(): WorkerImpl = WorkerImpl(this)
    override fun save(obj: Worker): Worker {
        obj as WorkerImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Worker? {
        return dao.findByIdOrNull(id)?.toBiz()
    }
}