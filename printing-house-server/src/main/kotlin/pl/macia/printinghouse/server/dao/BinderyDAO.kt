package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Bindery

interface BinderyDAO {
    fun findById(id: Int): Bindery?
    fun findByName(name: String): Bindery?
    fun create(bindery: Bindery)
}