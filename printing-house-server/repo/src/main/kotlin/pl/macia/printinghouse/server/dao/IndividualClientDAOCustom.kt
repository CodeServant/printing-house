package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.IndividualClient

internal interface IndividualClientDAOCustom {
    fun findByEmail(email: String): IndividualClient?
}