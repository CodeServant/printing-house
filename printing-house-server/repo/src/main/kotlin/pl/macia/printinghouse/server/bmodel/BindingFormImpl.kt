package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.BindingForm as PBindingForm

internal class BindingFormImpl(persistent: PBindingForm) : BindingForm, BusinessBase<PBindingForm>(persistent) {
    constructor(name: String) : this(
        PBindingForm(name)
    )

    override var bindingFormId: Int? by persistent::id
    override var name: String by persistent::name
}

fun BindingForm(name: String): BindingForm {
    return BindingFormImpl(name)
}