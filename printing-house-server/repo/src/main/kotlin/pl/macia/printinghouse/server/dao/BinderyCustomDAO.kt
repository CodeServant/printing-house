package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Bindery

interface BinderyCustomDAO {
    fun findByName(name: String): Bindery?
}