package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class ChangeResp(
    val changed: Boolean
)