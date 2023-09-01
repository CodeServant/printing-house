package pl.macia.printinghouse.server.bmodel

sealed interface PaperOrderType {
    var papOrdTypid: Int?
    var grammage: Double
    var stockCirculation: Int
    var sheetNumber: Int
    var comment: String?
    var circulation: Int
    var platesQuantityForPrinter: Int
    var paperType: PaperType
    var printer: Printer
    var colouring: Colouring
    var impositionType: ImpositionType
    var order: Order
    var size: Size
    var productionSize: Size
}