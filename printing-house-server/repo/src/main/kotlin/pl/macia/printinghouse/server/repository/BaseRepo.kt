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

sealed interface EmailRepo {
    fun findById(id: Int): Email?
    fun save(obj: Email): Email
}

internal sealed interface EmailIntRepo : EmailRepo, SingleIdRepo<Email, Int>
sealed interface IndividualClientRepo {
    fun save(obj: IndividualClient): IndividualClient
    fun findByClientId(clientId: Int): IndividualClient?
    fun findByPersonId(personId: Int): IndividualClient?
    fun Client.isIndividualClient(): Boolean
}

internal sealed interface IndividualClientIntRepo : IndividualClientRepo, BaseRepo<IndividualClient>, PersonRepos,
    ClientRepos

sealed interface BinderyRepo {
    fun findById(id: Int): Bindery?
    fun save(obj: Bindery): Bindery
}

internal sealed interface BinderyIntRepo : BinderyRepo, SingleIdRepo<Bindery, Int>
sealed interface BindingFormRepo {
    fun findById(id: Int): BindingForm?
    fun save(obj: BindingForm): BindingForm
}

internal sealed interface BindingFormIntRepo : BindingFormRepo, SingleIdRepo<BindingForm, Int>
sealed interface ColouringRepo {
    fun findById(id: Byte): Colouring?
    fun save(obj: Colouring): Colouring
}

internal sealed interface ColouringIntRepo : ColouringRepo, SingleIdRepo<Colouring, Byte>
sealed interface CompanyClientRepo {
    fun findByCompanyId(companyId: Int): CompanyClient?
    fun findByClientId(clientId: Int): CompanyClient?
    fun save(obj: CompanyClient): CompanyClient
    fun Client.isCompanyClient(): Boolean
}

internal sealed interface CompanyClientIntRepo : CompanyClientRepo, BaseRepo<CompanyClient>, ClientRepos

sealed interface EnoblingRepo {
    fun findById(id: Int): Enobling?
    fun save(obj: Enobling): Enobling
}

internal sealed interface EnoblingIntRepo : EnoblingRepo, SingleIdRepo<Enobling, Int>

sealed interface PunchRepo {
    fun findById(id: Int): Punch?
    fun save(obj: Punch): Punch
}

internal sealed interface PunchIntRepo : PunchRepo, SingleIdRepo<Punch, Int>
sealed interface UVVarnishRepo {
    fun findById(id: Int): UVVarnish?
    fun save(obj: UVVarnish): UVVarnish
}

internal sealed interface UVVarnishIntRepo : UVVarnishRepo, SingleIdRepo<UVVarnish, Int>
internal sealed interface ClientRepos {
    fun findByClientId(clientId: Int): Client?
}

/**
 * To save [Client], use specified repos of [Client] subtypes.
 */
sealed interface ClientRepo {
    fun findById(id: Int): Client?
}

internal sealed interface ClientIntRepo : ClientRepo
sealed interface ImpositionTypeRepo {
    fun findById(id: Int): ImpositionType?
    fun save(obj: ImpositionType): ImpositionType
}

internal sealed interface ImpositionTypeIntRepo : ImpositionTypeRepo, SingleIdRepo<ImpositionType, Int>
sealed interface PrinterRepo {
    fun save(obj: Printer): Printer
    fun findById(id: Int): Printer?
}

internal sealed interface PrinterIntRepo : PrinterRepo, SingleIdRepo<Printer, Int>

sealed interface RoleRepo {
    fun save(obj: Role): Role
    fun findById(id: Int): Role?
}

internal sealed interface RoleIntRepo : RoleRepo, SingleIdRepo<Role, Int>

sealed interface PaperTypeRepo {
    fun save(obj: PaperType): PaperType
    fun findById(id: Int): PaperType?
}

internal sealed interface PaperTypeIntRepo : PaperTypeRepo, SingleIdRepo<PaperType, Int>

sealed interface SizeRepo {
    fun save(obj: Size): Size
    fun findById(id: Int): Size?
}

internal sealed interface SizeIntRepo : SizeRepo, SingleIdRepo<Size, Int>

sealed interface URLRepo {
    fun save(obj: URL): URL
    fun findById(id: Long): URL?
}

internal sealed interface URLIntRepo : URLRepo, SingleIdRepo<URL, Long>

sealed interface WorkflowStageRepo {
    fun save(obj: WorkflowStage): WorkflowStage
    fun findById(id: Int): WorkflowStage?
}

internal sealed interface WorkflowStageIntRepo : WorkflowStageRepo, SingleIdRepo<WorkflowStage, Int>