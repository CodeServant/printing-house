package pl.macia.printinghouse.web.cli

import io.kvision.core.StringPair
import io.kvision.core.onChange
import io.kvision.form.ValidationStatus
import io.kvision.form.select.Select
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.select
import io.kvision.form.text.Text
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.ClientReq
import pl.macia.printinghouse.request.CompanyClientReq
import pl.macia.printinghouse.request.IndividualClientReq
import pl.macia.printinghouse.request.summary
import pl.macia.printinghouse.response.CompanyClientResp
import pl.macia.printinghouse.response.IndividualClientResp
import pl.macia.printinghouse.response.summary
import pl.macia.printinghouse.web.dao.ClientDao

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

/**
 * Controller to pick client if exists add if not exists.
 */
class PickClientForm() : SimplePanel() {
    private var unwindClientForm = AddButton()
    private val clientForm = InsertClientForm()
    private val tomSelClient = TomSelect(
        label = "Client",
        tsCallbacks = TomSelectCallbacks(
            load = { query, callback ->
                ClientDao().searchClient(
                    query,
                    onFulfilled = {
                        val arr = it.map { client ->
                            obj {
                                this.value = client.clientId
                                this.text = client.summary()
                            }
                        }.toTypedArray()
                        callback(arr)
                    },
                    onRejected = {
                        callback(arrayOf())
                    })
            }
        )
    )
    private val acceptClientForm = AddButton()

    init {
        this.add(tomSelClient)
        unwindClientForm.text = "new client"
        acceptClientForm.visible = false
        clientForm.visible = false
        unwindClientForm.onClick {
            toggleClientForm()
        }
        this.add(clientForm)
        this.add(acceptClientForm)
        this.add(unwindClientForm)
        acceptClientForm.onClick {
            val clientData: ClientReq? = clientForm.getFormData()
            if (clientData != null) {
                ClientDao().newClientReq(
                    clientData,
                    onFulfilled = { recId ->
                        insertToast("Client inserted succesfully")
                        tomSelClient.options = listOf(StringPair(recId.id.toString(), clientData.summary()))
                        tomSelClient.value = recId.id.toString()
                        toggleClientForm()
                    },
                    onRejected = {
                        failToast("There was error while inserting client", "Client error")
                    }
                )
            }
        }
    }

    private fun toggleClientForm() {
        clientForm.visible = !clientForm.visible
        acceptClientForm.visible = !acceptClientForm.visible
        unwindClientForm.visible = !unwindClientForm.visible
    }

    /**
     * Returns Client id as String
     */
    fun getFormData(): String? {
        return tomSelClient.getValue()
    }
}

class InsertClientForm() : SimplePanel() {
    private val clientTypeIn = Select(
        options = listOf(
            Pair(ClientType.COMPANY.toString(), ClientType.COMPANY.humanRead),
            Pair(ClientType.INDIVIDUAL.toString(), ClientType.INDIVIDUAL.humanRead),
        ), label = "Client Type"
    )
    private val phoneIn = TextInput("phone number")
    private val emailIn = TextInput("email")
    private val nameIn = TextInput("name")
    private val surnameIn = TextInput("surname")
    private val peselIn = TextInput("pesel")
    private val nipIn = TextInput("NIP")

    init {
        add(clientTypeIn)
        add(emailIn)
        add(phoneIn)
        nameIn.visible = false
        surnameIn.visible = false
        peselIn.visible = false
        nipIn.visible = false
        add(nameIn)
        add(surnameIn)
        add(peselIn)
        add(nipIn)


        clientTypeIn.onChange {
            val clientType = ClientType.valueOf(
                clientTypeIn.getValue() ?: throw RuntimeException("there is no option for the client type to be null")
            )
            resetAllFieldsErrors()
            when (clientType) {
                ClientType.INDIVIDUAL -> {
                    nameIn.visible = true
                    surnameIn.visible = true
                    peselIn.visible = true
                    nipIn.visible = false
                }

                ClientType.COMPANY -> {
                    surnameIn.visible = false
                    peselIn.visible = false
                    nameIn.visible = true
                    nipIn.visible = true
                }
            }
        }
    }

    fun getFormData(): ClientReq? {
        val nullMsg = "is null after validating data and forming request"
        when (clientTypeIn.getValue()) {
            ClientType.COMPANY.toString() -> {
                if (validateWhenCommpany())
                    return CompanyClientReq(
                        name = nameIn.getValue() ?: throw NullPointerException("name $nullMsg"),
                        phoneNumber = phoneIn.getValue(),
                        email = emailIn.getValue(),
                        nip = nipIn.getValue()?.toString() ?: throw NullPointerException("nip $nullMsg"),
                    )
            }

            ClientType.INDIVIDUAL.toString() -> {
                if (validateWhenIndyvifualClient())
                    return IndividualClientReq(
                        name = nameIn.getValue() ?: throw NullPointerException("name $nullMsg"),
                        surname = surnameIn.getValue() ?: throw NullPointerException("surname $nullMsg"),
                        psudoPESEL = peselIn.getValue() ?: throw NullPointerException("pesel $nullMsg"),
                        phoneNumber = phoneIn.getValue(),
                        email = emailIn.getValue(),
                    )
            }

            else -> {
                validateSelectedType()
            }
        }
        return null
    }

    private fun validateTextBlanc(textField: Text, paramName: String): Boolean {
        if (textField.getValue().isNullOrBlank()) {
            textField.validationStatus = ValidationStatus.INVALID
            textField.validatorError = "$paramName must not be blank"
        } else {
            textField.validationStatus = ValidationStatus.VALID
            textField.validatorError = null
            return true
        }
        return false
    }

    private fun validatePhoneOrEmail(): Boolean {
        if (emailIn.getValue().isNullOrBlank() && phoneIn.getValue().isNullOrBlank()) {
            emailIn.validationStatus = ValidationStatus.INVALID
            phoneIn.validationStatus = ValidationStatus.INVALID
            val err = "phone or email should be specified"
            emailIn.validatorError = err
            phoneIn.validatorError = err
            return false
        } else {
            emailIn.validationStatus = ValidationStatus.VALID
            phoneIn.validationStatus = ValidationStatus.VALID
            emailIn.validatorError = null
            phoneIn.validatorError = null
            return true
        }
    }

    private fun validateName(): Boolean {
        return validateTextBlanc(nameIn, "name")
    }

    private fun validateSurname(): Boolean {
        return validateTextBlanc(surnameIn, "surname")
    }

    private fun validateNIP(): Boolean {
        if (nipIn.getValue().isNullOrBlank() || nipIn.getValue()?.matches(Regex("\\d{10}")) == false) {
            nipIn.validationStatus = ValidationStatus.INVALID
            nipIn.validatorError = "NIP not properly formatted"
            return false
        } else {
            nipIn.validationStatus = ValidationStatus.VALID
            nipIn.validatorError = null
            return true

        }


    }

    private fun validatePesel(): Boolean {
        if (!validateTextBlanc(peselIn, "pesel")) {
            if (nameIn.getValue()?.length != 11) {
                nameIn.validationStatus = ValidationStatus.INVALID
                nameIn.validatorError = "pesel have to have 11 characters"
            } else {
                nameIn.validationStatus = ValidationStatus.VALID
                nameIn.validatorError = null
                return true
            }
        }
        return false
    }

    private fun validateWhenCommpany(): Boolean {
        var valid = true
        valid = validatePhoneOrEmail() && valid
        valid = validateName() && valid
        valid = validateNIP() && valid
        return valid
    }

    private fun validateWhenIndyvifualClient(): Boolean {
        var valid = true
        valid = validateName() && valid
        valid = validateSurname() && valid
        valid = validatePesel() && valid
        valid = validatePhoneOrEmail() && valid
        return valid
    }

    private fun validateSelectedType(): Boolean {
        if (clientTypeIn.getValue().isNullOrBlank()) {
            clientTypeIn.validatorError = "no client type selected"
            clientTypeIn.validationStatus = ValidationStatus.INVALID
            return false
        } else {
            resetSelectionTypeError()
            return true
        }
    }

    private fun resetSelectionTypeError() {
        clientTypeIn.validatorError = null
        clientTypeIn.validationStatus = null
    }

    private fun resetAllFieldsErrors() {
        resetSelectionTypeError()
        nameIn.validatorError = null
        nameIn.validationStatus = null
        surnameIn.validatorError = null
        surnameIn.validationStatus = null
        phoneIn.validatorError = null
        phoneIn.validationStatus = null
        emailIn.validatorError = null
        emailIn.validationStatus = null
        nipIn.validatorError = null
        nipIn.validationStatus = null
        peselIn.validatorError = null
        peselIn.validationStatus = null
    }
}