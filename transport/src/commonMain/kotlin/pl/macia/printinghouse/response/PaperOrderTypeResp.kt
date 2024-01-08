package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class PaperOrderTypeResp (
    val id: Int,
    val grammage: Double,
    val stockCirculation: Int,
    val sheetNumber: Int,
    val comment: String?,
    val circulation: Int,
    val platesQuantityForPrinter: Int,
    val paperType: PaperTypeResp,
    val printer: PrinterResp,
    val colouring: ColouringResp,
    val impositionType: ImpositionTypeResp,
    val size: SizeResp,
    val productionSize: SizeResp,
)
