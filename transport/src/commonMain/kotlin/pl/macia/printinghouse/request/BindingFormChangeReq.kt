package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class BindingFormChangeReq(
    val name: String
)