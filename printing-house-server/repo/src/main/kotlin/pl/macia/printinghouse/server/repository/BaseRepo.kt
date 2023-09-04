package pl.macia.printinghouse.server.repository

import pl.macia.printinghouse.server.bmodel.*

/**
 * End repository contract. Minimal requirements for repositories.
 */
internal sealed interface BaseRepo<T> {
    fun save(obj: T): T
}

internal sealed interface SingleIdRepo<T, ID> : BaseRepo<T> {
    fun findById(id: ID): T?
}

/**
 * Interface for all repositories of [Person] child objects.
 */
internal sealed interface PersonRepos {
    fun findByPersonId(personId: Int): Person?
}

interface EmailRepo {
    fun findById(id: Int): Email?
    fun save(obj: Email): Email
}

internal interface EmailIntRepo : EmailRepo, SingleIdRepo<Email, Int>
interface IndividualClientRepo {
    fun save(obj: IndividualClient): IndividualClient
    fun findByClientId(clientId: Int): IndividualClient?
    fun findByPersonId(personId: Int): IndividualClient?
}

internal interface IndividualClientIntRepo : IndividualClientRepo, BaseRepo<IndividualClient>
interface BinderyRepo {
    fun findById(id: Int): Bindery?
    fun save(obj: Bindery): Bindery
}

internal interface BinderyIntRepo : BinderyRepo, SingleIdRepo<Bindery, Int>
interface BindingFormRepo {
    fun findById(id: Int): BindingForm?
    fun save(obj: BindingForm): BindingForm
}

internal interface BindingFormIntRepo : BindingFormRepo, SingleIdRepo<BindingForm, Int>
interface ColouringRepo {
    fun findById(id: Byte): Colouring?
    fun save(obj: Colouring): Colouring
}

internal interface ColouringIntRepo : ColouringRepo, SingleIdRepo<Colouring, Byte>
interface CompanyClientRepo {
    fun findByCompanyId(companyId: Int): CompanyClient?
    fun findByClientId(clientId: Int): CompanyClient?
    fun save(obj: CompanyClient): CompanyClient
}

internal interface CompanyClientIntRepo : CompanyClientRepo, BaseRepo<CompanyClient>