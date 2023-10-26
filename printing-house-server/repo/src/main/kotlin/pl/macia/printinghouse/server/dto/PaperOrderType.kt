package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*

@Poko
@Entity
@Table(name = PaperOrderType.TABLE_NAME)
internal class PaperOrderType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    var id: Int? = null,
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = TYPE_ID)
    var paperType: PaperType,
    @Column(name = GRAMMAGE)
    var grammage: Double,
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = COLOURS)
    var colours: Colouring,
    @Column(name = CIRCULATION)
    var circulation: Int,
    @Column(name = STOCK_CIRCULATION)
    var stockCirculation: Int,
    @Column(name = SHEET_NUMBER)
    var sheetNumber: Int,
    @Nullable
    @jakarta.validation.constraints.Size(max = 1000)
    @Column(name = COMMENT)
    var comment: String?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = PRINTER)
    var printer: Printer,
    @Column(name = PLATES_QUANTITY_FOR_PRINTER)
    var platesQuantityForPrinter: Int,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = IMPOSITION)
    var imposition: ImpositionType,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = SIZE)
    var size: Size,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = PRODUCTION_SIZE)
    var productionSize: Size,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ORDER_ID, nullable = false)
    var order: Order
) {
    companion object {
        const val TABLE_NAME = "PaperOrderType"
        const val ID = "id"
        const val TYPE_ID = "typeId"
        const val ORDER_ID = "orderId"
        const val GRAMMAGE = "grammage"
        const val COLOURS = "colours"
        const val CIRCULATION = "circulation"
        const val STOCK_CIRCULATION = "stockCirculation"
        const val SHEET_NUMBER = "sheetNumber"
        const val COMMENT = "comment"
        const val PRINTER = "printer"
        const val PLATES_QUANTITY_FOR_PRINTER = "platesQuantityForPrinter"
        const val IMPOSITION = "imposition"
        const val SIZE = "size"
        const val PRODUCTION_SIZE = "productionSize"
        const val ORDER_FIELD = "order"
    }

    constructor(
        paperType: PaperType,
        grammage: Double,
        colours: Colouring,
        circulation: Int,
        stockCirculation: Int,
        sheetNumber: Int,
        comment: String?,
        printer: Printer,
        platesQuantityForPrinter: Int,
        imposition: ImpositionType,
        size: Size,
        productionSize: Size,
        order: Order
    ) : this(
        null,
        paperType,
        grammage,
        colours,
        circulation,
        stockCirculation,
        sheetNumber,
        comment,
        printer,
        platesQuantityForPrinter,
        imposition,
        size,
        productionSize,
        order
    )
}