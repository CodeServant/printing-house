package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PaperOrderTypeReq(
    val grammage: Double,
    val stockCirculation: Int,
    val sheetNumber: Int,
    val comment: String?,
    val circulation: Int,
    val platesQuantityForPrinter: Int,
    val paperTypeId: Int,
    val printerId: Int,
    val colouring: ColouringPartReq,
    val impositionTypeId: Int,
    val size: SizeReq,
    val productionSize: SizeReq
)

@Serializable
data class ColouringPartReq(val firstSide: Byte, val secondSide: Byte)