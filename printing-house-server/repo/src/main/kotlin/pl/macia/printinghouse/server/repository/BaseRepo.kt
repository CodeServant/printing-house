package pl.macia.printinghouse.server.repository

import pl.macia.printinghouse.server.bmodel.*

/**
 * End repository contract. Minimal requirements for repositories.
 */
sealed interface BaseRepo<T> {
    fun save(obj: T): T
}

sealed interface SingleIdRepo<T, ID> : BaseRepo<T> {
    fun findById(id: ID): T?
}

/**
 * Interface for all repositories of [Person] child objects.
 */
sealed interface PersonRepos {
    fun findByPersonId(personId: Int): Person?
}

interface EmailRepo : SingleIdRepo<Email, Int>
interface IndividualClientRepo : BaseRepo<IndividualClient>, PersonRepos {
    fun findByClientId(clientId: Int): IndividualClient?
    override fun findByPersonId(personId: Int): IndividualClient?
}

interface BinderyRepo : SingleIdRepo<Bindery, Int>