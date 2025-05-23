package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Salesman

internal interface SalesmanDAO : JpaRepository<Salesman, Int>, SalesmanDAOCustom {
    fun findByEmployed(emp: Boolean): List<Salesman>
}