package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.annotation.Nullable
import jakarta.persistence.*

const val tablePaperOrderType = "PaperOrderType"
const val paperOrderTypeId = "id"
const val paperOrderTypeTypeId = "typeId"
const val paperOrderTypeOrderId = "orderId"
const val paperOrderTypeGrammage = "grammage"
const val paperOrderTypeColours = "colours"
const val paperOrderTypeCirculation = "circulation"
const val paperOrderTypeStockCirculation = "stockCirculation"
const val paperOrderTypeSheetNumber = "sheetNumber"
const val paperOrderTypeComment = "comment"
const val paperOrderTypePrinter = "printer"
const val paperOrderTypePlatesQuantityForPrinter = "platesQuantityForPrinter"
const val paperOrderTypeImposition = "imposition"
const val paperOrderTypeSize = "size"
const val paperOrderTypeProductionSize = "productionSize"
const val paperOrderTypeOrderField = "order"

@Poko
@Entity
@Table(name = tablePaperOrderType)
class PaperOrderType internal constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = paperOrderTypeId, nullable = false)
    var id: Int? = null,
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = paperOrderTypeTypeId)
    var paperType: PaperType,
    @Column(name = paperOrderTypeGrammage)
    var grammage: Double,
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = paperOrderTypeColours)
    var colours: Colouring,
    @Column(name = paperOrderTypeCirculation)
    var circulation: Int,
    @Column(name = paperOrderTypeStockCirculation)
    var stockCirculation: Int,
    @Column(name = paperOrderTypeSheetNumber)
    var sheetNumber: Int,
    @Nullable
    @jakarta.validation.constraints.Size(max = 1000)
    @Column(name = paperOrderTypeComment)
    var comment: String?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = paperOrderTypePrinter)
    var printer: Printer,
    @Column(name = paperOrderTypePlatesQuantityForPrinter)
    var platesQuantityForPrinter: Int,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = paperOrderTypeImposition)
    var imposition: ImpositionType,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = paperOrderTypeSize)
    var size: Size,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = paperOrderTypeProductionSize)
    var productionSize: Size,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = paperOrderTypeOrderId, nullable = false)
    val order: Order
) {
    internal constructor(
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