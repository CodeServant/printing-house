package pl.macia.printinghouse.server.repository

import pl.macia.printinghouse.server.bmodel.Bindery
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.IndividualClient

interface BaseRepo<T, ID> {
    fun findById(id: ID): T?
    fun save(obj: T): T
}

interface EmailRepo : BaseRepo<Email, Int>
interface IndividualClientRepo : BaseRepo<IndividualClient, Int>
interface BinderyRepo : BaseRepo<Bindery, Int>