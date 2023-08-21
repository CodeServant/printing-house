package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.validation.constraints.Size as JpaSize

const val tableOrder = "`Order`"
const val orderId = "id"
const val orderName = "name"
const val orderNetSize = "netSize"
const val orderPages = "pages"
const val orderSupervisor = "supervisor"
const val orderClient = "client"
const val orderCreationDate = "creationDate"
const val orderRealizationDate = "realizationDate"
const val orderBindingForm = "bindingForm"
const val orderBindery = "bindery"
const val orderFolding = "folding"
const val orderTowerCut = "towerCut"
const val orderImageURL = "imageURL"
const val orderImageComment = "imageComment"
const val orderChecked = "checked"
const val orderDesignsNumberForSheet = "designsNumberForSheet"
const val orderCompletionDate = "completionDate"
const val orderWithdrawalDate = "withdrawalDate"
const val orderComment = "comment"

@Poko
@Entity
@Table(name = tableOrder)
class Order private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = orderId)
    var id: Int?,
    @Column(name = orderName, nullable = false)
    @field:JpaSize(max = 200)
    @field:NotBlank
    var name: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderNetSize, nullable = false)
    var netSize: Size,
    @Column(name = orderPages, nullable = false)
    @field:Positive
    var pages: Int,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderSupervisor, nullable = false)
    var supervisor: Salesman,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderClient, nullable = false)
    var client: Client,
    @Column(name = orderCreationDate, nullable = false)
    var creationDate: LocalDateTime,
    @Column(name = orderRealizationDate, nullable = false)
    var realizationDate: LocalDateTime,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderBindingForm, nullable = false)
    var bindingForm: BindingForm,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderBindery, nullable = false, referencedColumnName = binderyId)
    var bindery: Bindery,
    @Column(name = orderFolding, nullable = false)
    var folding: Boolean,
    @Column(name = orderTowerCut, nullable = false)
    var towerCut: Boolean,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderImageURL, nullable = true)
    var imageURL: URL?,
    @Column(name = orderImageComment, nullable = true)
    @field:JpaSize(min = 1)
    var imageComment: String?,
    @Column(name = orderChecked, nullable = false)
    var checked: Boolean,
    @Column(name = orderDesignsNumberForSheet, nullable = false)
    @field:PositiveOrZero
    var designsNumberForSheet: Int,
    @Column(name = orderCompletionDate, nullable = true)
    var completionDate: LocalDateTime?,
    @Column(name = orderWithdrawalDate, nullable = true)
    var withdrawalDate: LocalDateTime?,
    @Column(name = orderComment, nullable = true)
    @field:JpaSize(min = 1)
    var comment: String?,
    @OneToOne(cascade = [CascadeType.ALL], mappedBy = calculationCardOrderField, orphanRemoval = true)
    var calculationCard: CalculationCard?,
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = paperOrderTypeOrderField,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    @field:JpaSize(min = 1)
    val paperOrderTypes: MutableList<PaperOrderType> = mutableListOf(),
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = orderEnoblingOrderField,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    val orderEnoblings: MutableList<OrderEnobling> = mutableListOf(),
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = workflowStageStopOrderField,
        cascade = [CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH]
    )
    @field:JpaSize(min = 1)
    val workflowStageStops: MutableList<WorkflowStageStop> = mutableListOf()
) {
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
        imageURL: URL?,
        imageComment: String?,
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
        imageComment,
        checked,
        designsNumberForSheet,
        completionDate,
        withdrawalDate,
        comment,
        calculationCard
    )

    /**
     * Adds new [tableWorkflowStageStop] with association to this [Order]
     */
    fun addWorkflowStageStop(
        comment: String?,
        createTime: LocalDateTime,
        assignTime: LocalDateTime?,
        worker: Worker?,
        workflowStage: WorkflowStage,
        lastWorkflowStage: Boolean
    ): WorkflowStageStop {
        val workflowStageStop = WorkflowStageStop(
            comment,
            createTime,
            assignTime,
            worker,
            workflowStage,
            lastWorkflowStage,
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
     * Creates [CalculationCard] or alters it.
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