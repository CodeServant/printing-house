package pl.macia.printinghouse.web.cli

import io.kvision.form.select.select
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.CompanyClientResp
import pl.macia.printinghouse.response.IndividualClientResp

enum class ClientType(val humanRead: String) {
    INDIVIDUAL("Individual"), COMPANY("Company")
}

@Serializable
data class ClientSummary(
    var clientId: Int,
    var name: String,
    var identity: String,
    var email: String?,
    var phoneNumber: String?,
    var type: ClientType
)

class ClientTab : SimplePanel() {
    init {
        val responses = mapOf(
            2 to IndividualClientResp(
                name = "Brad",
                surname = "Pitt",
                psudoPESEL = "pseudopesel",
                personId = 1,
                clientId = 2,
                email = "brad@examples.com",
                phoneNumber = "+48 000000000"
            ),
            1 to CompanyClientResp(
                clientId = 1,
                phoneNumber = null,
                email = "exill.corp@example.com",
                companyId = 1,
                nip = "0000000000",
                name = "evil corp sp. z.ł.o.",
            )
        )
        val summaryList: List<ClientSummary> = responses.values.map {
            when (it) {
                is CompanyClientResp -> ClientSummary(
                    clientId = it.clientId,
                    name = it.name,
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    identity = it.nip,
                    type = ClientType.COMPANY
                )

                is IndividualClientResp -> ClientSummary(
                    clientId = it.clientId,
                    name = "${it.surname} ${it.name}",
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    identity = it.psudoPESEL,
                    type = ClientType.INDIVIDUAL
                )
            }
        }
        val clientContext = ObservableValue<ClientSummary?>(null)

        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Email", "email"),
                ColumnDefinition("Phone Number", "phoneNumber"),
                ColumnDefinition("NIP/PESEL", "identity"),
                ColumnDefinition("Type", "type"),
            ),
            onSelected = {
                clientContext.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = TextInput("Name")
                    val surname = TextInput("Surname")
                    val email = TextInput("Email")
                    val phone = TextInput("Phone Number")
                    val nip = TextInput("NIP")
                    val pesel = TextInput("PESEL")


                    val selecteedType = select(
                        options = listOf(
                            Pair(ClientType.COMPANY.toString(), "Company"),
                            Pair(ClientType.INDIVIDUAL.toString(), "Individual Client"),
                        ),
                        label = "Client Type"
                    ) {
                        clientContext.subscribe {
                            value = it?.type.toString()
                            disabled = it != null
                        }
                    }
                    clientContext.subscribe { cliSumm ->
                        selecteedType.subscribe {
                            val cliId = cliSumm?.clientId
                            val cliResp = if (cliId != null) responses[cliId] else null
                            when (it) {
                                ClientType.COMPANY.toString() -> {

                                    add(name)
                                    add(email)
                                    add(phone)
                                    add(nip)
                                    (cliResp as? CompanyClientResp)?.apply {
                                        name.value = this.name
                                        email.value = this.email
                                        phone.value = this.phoneNumber
                                        nip.value = this.nip
                                    }
                                }

                                ClientType.INDIVIDUAL.toString() -> {
                                    add(name)
                                    add(surname)
                                    add(email)
                                    add(phone)
                                    add(pesel)
                                    (cliResp as? IndividualClientResp)?.apply {
                                        name.value = this.name
                                        email.value = this.email
                                        phone.value = this.phoneNumber
                                        pesel.value = this.psudoPESEL
                                        surname.value = this.surname
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}