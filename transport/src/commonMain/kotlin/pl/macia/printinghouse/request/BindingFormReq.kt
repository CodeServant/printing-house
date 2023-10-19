package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class BindingFormReq(
    val name: String
)