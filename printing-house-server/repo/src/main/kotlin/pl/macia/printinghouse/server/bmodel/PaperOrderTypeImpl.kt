package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.PaperOrderType as PPaperOrderType

internal class PaperOrderTypeImpl(p: PPaperOrderType) : PaperOrderType, BusinessBase<PPaperOrderType>(p) {
    //TODO constructor
    override var papOrdTypid: Int? by persistent::id
    override var grammage: Double by persistent::grammage
    override var stockCirculation: Int by persistent::stockCirculation
    override var sheetNumber: Int by persistent::sheetNumber
    override var comment: String? by persistent::comment
    override var circulation: Int by persistent::circulation
    override var platesQuantityForPrinter: Int by persistent::platesQuantityForPrinter
    override var paperType: PaperType by delegate(persistent.paperType, ::PaperTypeImpl, PaperType::class.java)
    override var printer: Printer by delegate(persistent.printer, ::PrinterImpl, Printer::class.java)
    override var colouring: Colouring by delegate(persistent.colours, ::ColouringImpl, Colouring::class.java)
    override var impositionType: ImpositionType by delegate(
        persistent.imposition,
        ::ImpositionTypeImpl,
        ImpositionType::class.java
    )
    override var order: Order
        get() = TODO("Not yet implemented")
        set(value) {}
    override var size: Size by delegate(persistent.size, ::SizeImpl, Size::class.java)
    override var productionSize: Size by delegate(persistent.productionSize, ::SizeImpl, Size::class.java)

}

internal fun toBizPaperOrderType(ppap: MutableList<PPaperOrderType>): BMutableList<PaperOrderType, PPaperOrderType> {
    return BMutableList(::PaperOrderTypeImpl, {
        it as PaperOrderTypeImpl
        it.persistent
    }, ppap)
}