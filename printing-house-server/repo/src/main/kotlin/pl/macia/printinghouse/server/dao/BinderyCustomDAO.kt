package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Bindery

internal interface BinderyCustomDAO {
    fun findByName(name: String): Bindery?
}