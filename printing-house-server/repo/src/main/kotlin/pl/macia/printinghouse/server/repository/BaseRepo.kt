package pl.macia.printinghouse.server.repository

import pl.macia.printinghouse.server.bmodel.Bindery
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.IndividualClient

interface BaseRepo<T> {
    fun save(obj: T): T
}

interface SingleIdRepo<T, ID> : BaseRepo<T> {
    fun findById(id: ID): T?
}

interface EmailRepo : BaseRepo<Email>, SingleIdRepo<Email, Int>
interface IndividualClientRepo : BaseRepo<IndividualClient> {
    fun findByClientId(clientId: Int): IndividualClient?
    fun findByPersonId(personId: Int): IndividualClient?
}

interface BinderyRepo : BaseRepo<Bindery>, SingleIdRepo<Bindery, Int>