package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.PaperOrderType as PPaperOrderType

internal class PaperOrderTypeImpl(p: PPaperOrderType) : PaperOrderType, BusinessBase<PPaperOrderType>(p) {
    constructor(
        paperType: PaperTypeImpl,
        grammage: Double,
        colours: ColouringImpl,
        circulation: Int,
        stockCirculation: Int,
        sheetNumber: Int,
        comment: String?,
        printer: PrinterImpl,
        platesQuantityForPrinter: Int,
        imposition: ImpositionTypeImpl,
        size: SizeImpl,
        productionSize: SizeImpl,
        order: OrderImpl
    ) : this(
        PPaperOrderType(
            paperType.persistent,
            grammage,
            colours.persistent,
            circulation,
            stockCirculation,
            sheetNumber,
            comment,
            printer.persistent,
            platesQuantityForPrinter,
            imposition.persistent,
            size.persistent,
            size.width,
            size.heigth,
            productionSize.persistent,
            productionSize.width,
            productionSize.heigth,
            order.persistent
        )
    )

    override var papOrdTypid: Int? by persistent::id
    override var grammage: Double by persistent::grammage
    override var stockCirculation: Int by persistent::stockCirculation
    override var sheetNumber: Int by persistent::sheetNumber
    override var comment: String? by persistent::comment
    override var circulation: Int by persistent::circulation
    override var platesQuantityForPrinter: Int by persistent::platesQuantityForPrinter
    override var paperType: PaperType by delegate(persistent::paperType, ::PaperTypeImpl, PaperType::class.java)
    override var printer: Printer by delegate(persistent::printer, ::PrinterImpl, Printer::class.java)
    override var colouring: Colouring by delegate(persistent::colours, ::ColouringImpl, Colouring::class.java)
    override var impositionType: ImpositionType by delegate(
        persistent::imposition,
        ::ImpositionTypeImpl,
        ImpositionType::class.java
    )
    override val order: Order by delegate(persistent::order, ::OrderImpl, Order::class.java)
    override var size: Size
        get() = if (persistent.size != null) SizeImpl(persistent.size!!) else SizeImpl(
            persistent.sizeWidth!!,
            persistent.sizeHeight!!
        )
        set(value) {
            fun setNew(newSize: SizeImpl) {
                persistent.size = newSize.persistent
                persistent.sizeWidth = newSize.width
                persistent.sizeHeight = newSize.heigth
            }
            if (value.sizeId == null && value.name == null) {
                setNew(SizeImpl(value.width, value.heigth))
            } else if (value.sizeId == null && value.name != null) {
                setNew(SizeImpl(value.name!!, value.width, value.heigth))
            } else if (value.sizeId != null && value.name != null) {
                setNew(value as SizeImpl)
            }
        }
    override var productionSize: Size
        get() = if (persistent.productionSize != null) SizeImpl(persistent.productionSize!!) else SizeImpl(
            persistent.productionSizeWidth!!,
            persistent.productionSizeHeight!!
        )
        set(value) {
            fun setNew(newSize: SizeImpl) {
                persistent.productionSize = newSize.persistent
                persistent.productionSizeWidth = newSize.width
                persistent.productionSizeHeight = newSize.heigth
            }
            if (value.sizeId == null && value.name == null) {
                setNew(SizeImpl(value.width, value.heigth))
            } else if (value.sizeId == null && value.name != null) {
                setNew(SizeImpl(value.name!!, value.width, value.heigth))
            } else if (value.sizeId != null && value.name != null) {
                setNew(value as SizeImpl)
            }
        }

}

internal fun toBizPaperOrderType(ppap: MutableList<PPaperOrderType>): BMutableList<PaperOrderType, PPaperOrderType> {
    return BMutableList(::PaperOrderTypeImpl, {
        it as PaperOrderTypeImpl
        it.persistent
    }, ppap)
}