package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Company

internal interface CompanyDAOCustom {
    fun findClientById(id: Int): Company?
}