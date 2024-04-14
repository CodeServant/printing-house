package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Salesman

internal interface SalesmanDAOCustom {
    fun findByEmail(email: String): Salesman?
}