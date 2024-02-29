package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Worker

internal interface WorkerDAOCustom {
    fun findByEmail(email: String): Worker?
}