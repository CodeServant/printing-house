package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.validation.constraints.Size as JpaSize

@Poko
@Entity
@Table(name = Order.TABLE_NAME)
internal class Order private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @Column(name = NAME, nullable = false)
    @field:JpaSize(max = 200)
    @field:NotBlank
    var name: String,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = NET_SIZE, nullable = false)
    var netSize: Size,
    @Column(name = PAGES, nullable = false)
    @field:Positive
    var pages: Int,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = SUPERVISOR, nullable = false)
    var supervisor: Salesman,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = CLIENT, nullable = false)
    var client: Client,
    @Column(name = CREATION_DATE, nullable = false)
    var creationDate: LocalDateTime,
    @Column(name = REALIZATION_DATE, nullable = false)
    var realizationDate: LocalDateTime,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BINDING_FORM, nullable = false)
    var bindingForm: BindingForm,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BINDERY, nullable = false, referencedColumnName = Bindery.ID)
    var bindery: Bindery,
    @Column(name = FOLDING, nullable = false)
    var folding: Boolean,
    @Column(name = TOWER_CUT, nullable = false)
    var towerCut: Boolean,
    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH]
    )
    @JoinColumn(name = IMAGE_URL, nullable = true)
    var imageURL: Image?,
    @Column(name = CHECKED, nullable = false)
    var checked: Boolean,
    @Column(name = DESIGNS_NUMBER_FOR_SHEET, nullable = false)
    @field:PositiveOrZero
    var designsNumberForSheet: Int,
    @Column(name = COMPLETION_DATE, nullable = true)
    var completionDate: LocalDateTime?,
    @Column(name = WITHDRAWAL_DATE, nullable = true)
    var withdrawalDate: LocalDateTime?,
    @Column(name = COMMENT, nullable = true)
    @field:JpaSize(min = 1)
    var comment: String?,
    @OneToOne(cascade = [CascadeType.ALL], mappedBy = CalculationCard.ORDER_FIELD, orphanRemoval = true)
    var calculationCard: CalculationCard?,
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = PaperOrderType.ORDER_FIELD,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    @field:JpaSize(min = 1)
    val paperOrderTypes: MutableList<PaperOrderType> = mutableListOf(),
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = OrderEnobling.ORDER_FIELD,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    val orderEnoblings: MutableList<OrderEnobling> = mutableListOf(),
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = WorkflowStageStop.ORDER_FIELD,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    @field:JpaSize(min = 1)
    val workflowStageStops: MutableList<WorkflowStageStop> = mutableListOf()
) {
    companion object {
        const val TABLE_NAME = "`Order`"
        const val ID = "id"
        const val NAME = "name"
        const val NET_SIZE = "netSize"
        const val PAGES = "pages"
        const val SUPERVISOR = "supervisor"
        const val CLIENT = "client"
        const val CREATION_DATE = "creationDate"
        const val REALIZATION_DATE = "realizationDate"
        const val BINDING_FORM = "bindingForm"
        const val BINDERY = "bindery"
        const val FOLDING = "folding"
        const val TOWER_CUT = "towerCut"
        const val IMAGE_URL = "imageURL"
        const val CHECKED = "checked"
        const val DESIGNS_NUMBER_FOR_SHEET = "designsNumberForSheet"
        const val COMPLETION_DATE = "completionDate"
        const val WITHDRAWAL_DATE = "withdrawalDate"
        const val COMMENT = "comment"
    }

    constructor(
        name: String,
        netSize: Size,
        pages: Int,
        supervisor: Salesman,
        client: Client,
        creationDate: LocalDateTime,
        realizationDate: LocalDateTime,
        bindingForm: BindingForm,
        bindery: Bindery,
        folding: Boolean,
        towerCut: Boolean,
        imageURL: Image?,
        checked: Boolean,
        designsNumberForSheet: Int,
        completionDate: LocalDateTime?,
        withdrawalDate: LocalDateTime?,
        comment: String?,
        calculationCard: CalculationCard?,
    ) : this(
        null,
        name,
        netSize,
        pages,
        supervisor,
        client,
        creationDate,
        realizationDate,
        bindingForm,
        bindery,
        folding,
        towerCut,
        imageURL,
        checked,
        designsNumberForSheet,
        completionDate,
        withdrawalDate,
        comment,
        calculationCard
    )

    /**
     * Adds new [WorkflowStageStop] with association to this [Order]
     */
    fun addWorkflowStageStop(
        comment: String?,
        createTime: LocalDateTime,
        assignTime: LocalDateTime?,
        worker: Worker?,
        workflowStage: WorkflowStage,
    ): WorkflowStageStop {
        val workflowStageStop = WorkflowStageStop(
            comment,
            createTime,
            assignTime,
            worker,
            workflowStage,
            this
        )
        workflowStageStops.add(workflowStageStop)
        return workflowStageStop
    }

    /**
     * Adds new [OrderEnobling] with association to this [Order]
     */
    fun addOrderEnobling(
        enobling: Enobling,
        bindery: Bindery,
        annotation: String?
    ): OrderEnobling {
        val ordEnob = OrderEnobling(
            enobling,
            bindery,
            annotation,
            this
        )
        orderEnoblings.add(ordEnob)
        return ordEnob
    }

    /**
     * Adds new [PaperOrderType] with association to this [Order]
     */
    fun addPaperOrderType(
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
        productionSize: Size
    ): PaperOrderType {
        val papOrdTp = PaperOrderType(
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
            this
        )
        paperOrderTypes.add(papOrdTp)
        return papOrdTp
    }

    /**
     * Creates new [CalculationCard].
     */
    fun setCalculationCard(
        bindingCost: BigDecimal,
        enobling: BigDecimal,
        otherCosts: BigDecimal,
        transport: BigDecimal
    ): CalculationCard {
        val calC = CalculationCard(
            bindingCost,
            enobling,
            otherCosts,
            transport,
            this
        )
        calculationCard = calC
        return calC
    }
}