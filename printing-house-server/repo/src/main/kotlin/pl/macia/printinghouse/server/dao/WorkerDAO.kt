package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Email
import pl.macia.printinghouse.server.dto.Worker

internal interface WorkerDAO : JpaRepository<Worker, Int>, WorkerDAOCustom {
    fun findByEmail(email: Email): Worker?
}