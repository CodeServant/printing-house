package pl.macia.printinghouse.server.bmodel

sealed interface BindingForm {
    var bindingFormId: Int?
    var name: String
}