package pl.macia.printinghouse.server.repository

interface BaseRepo<T, ID> {
    fun findById(id: ID): T?
    fun save(obj: T): T
}