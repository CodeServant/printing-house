package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class BindingFormResp(
    val id: Int,
    val name: String
)