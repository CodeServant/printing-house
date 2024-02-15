package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.OrderReq
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.*

@Service
class OrderService {
    @Autowired
    private lateinit var repo: OrderRepo

    @Autowired
    private lateinit var clientRepo: ClientRepo

    @Autowired
    private lateinit var sizeRepo: SizeRepo

    @Autowired
    private lateinit var binderyRepo: BinderyRepo

    @Autowired
    private lateinit var salesmanRepo: SalesmanRepo

    @Autowired
    private lateinit var bindingFormRepo: BindingFormRepo

    @Autowired
    private lateinit var graphRepo: WorkflowDirGraphRepo

    @Autowired
    private lateinit var enoblingRepo: EnoblingRepo

    @Autowired
    private lateinit var paperTypeRepo: PaperTypeRepo

    @Autowired
    private lateinit var printerRepo: PrinterRepo

    @Autowired
    private lateinit var colouringRepo: ColouringRepo

    @Autowired
    private lateinit var impositionTypeRepo: ImpositionTypeRepo

    fun findById(id: Int): OrderResp? {
        val found = repo.findById(id)
        return found?.toTransport(clientRepo)
    }

    /**
     * Inserts new order.
     * @throws ConversionException if data built not properly or not found in database
     */
    @Transactional
    fun insertNew(req: OrderReq): RecID { //todo simplify this function
        val fetchedClient =
            clientRepo.findById(req.clientId) ?: throw ConversionException("${req::clientId.name} not in database")
        val fetchedNetSize = sizeRepo.createByParameters(req.netSize.width, req.netSize.heigth)

        val image: Image? = if (req.imageUrl != null)
            Image(req.imageUrl!!.url, req.imageUrl?.comment)
        else null
        val fetchedBindery =
            binderyRepo.findById(req.binderyId) ?: throw ConversionException("${req::binderyId.name} not in database")
        val fetchedSalesman = salesmanRepo.findById(req.salesmanId)
            ?: throw ConversionException("${req::salesmanId.name} not in database")
        val fetchedBindingForm = bindingFormRepo.findById(req.bindingFormId)
            ?: throw ConversionException("${req::binderyId.name} not in database")
        var orderCreated = Order(
            name = req.name,
            netSize = fetchedNetSize,
            comment = req.comment,
            withdrawalDate = req.withdrawalDate?.toJavaLocalDateTime(),
            completionDate = req.completionDate?.toJavaLocalDateTime(),
            designsNumberForSheet = req.designsNumberForSheet,
            checked = req.checked,
            towerCut = req.towerCut,
            folding = req.folding,
            client = fetchedClient,
            realizationDate = req.realizationDate.toJavaLocalDateTime(),
            caretionDate = req.creationDate.toJavaLocalDateTime(),
            pages = req.pages,
            imageUrl = image,
            bindery = fetchedBindery,
            salesman = fetchedSalesman,
            bindingForm = fetchedBindingForm
        )

        // adding workflow stage stops
        val fetchedGraph = graphRepo.findById(req.workflowDirGraphId)
            ?: throw ConversionException("${req::workflowDirGraphId.name} not in database")

        // adding PaperOrderTypee
        req.paperOrderTypes.forEach {
            orderCreated.addPaperOrderType(
                it.grammage,
                it.stockCirculation,
                it.sheetNumber,
                it.comment,
                it.circulation,
                it.platesQuantityForPrinter,
                paperType = paperTypeRepo.findById(it.paperTypeId)
                    ?: throw ConversionException("${it::paperTypeId.name}: ${it.paperTypeId} not found in database"),
                printer = printerRepo.findById(it.printerId)
                    ?: throw ConversionException("${it::printerId.name}: ${it.printerId} not found in database"),
                colouring = colouringRepo.findById(it.colouringId)
                    ?: throw ConversionException("${it::colouringId.name}: ${it.colouringId} not found in database"),
                impositionType = impositionTypeRepo.findById(it.impositionTypeId)
                    ?: throw ConversionException("${it::impositionTypeId.name}: ${it.impositionTypeId} is not in database"),
                size = sizeRepo.createByParameters(it.size.width, it.size.heigth),
                productionSize = sizeRepo.createByParameters(it.productionSize.width, it.productionSize.heigth)
            )
        }

        fetchedGraph.startEdges.forEach {
            orderCreated.addWorkflowStageStop(
                comment = null,
                assignTime = null,
                createTime = java.time.LocalDateTime.now(),
                worker = null,
                workflowDirEdge = it
            )
        }
        repo.save(orderCreated)

        // adding calculation card
        req.calculationCard?.let {
            try {
                orderCreated.setCalculationCard(
                    transport = it.transport.toBigDecimal(),
                    otherCosts = it.otherCosts.toBigDecimal(),
                    enoblingCost = it.enoblingCost.toBigDecimal(),
                    bindingCost = it.bindingCost.toBigDecimal()
                )
                // adding printcosts
                it.printCosts.forEach { printCost ->
                    orderCreated.calculationCard?.addPrintCost(
                        printCost.printCost.toBigDecimal(),
                        printCost.matrixCost.toBigDecimal(),
                        printerRepo.findById(printCost.printerId)
                            ?: throw ConversionException("${printCost::printerId.name}: ${printCost.printerId} not found in database")
                    )
                }

            } catch (e: NumberFormatException) {
                throw ConversionException(e.message)
            }

        }

        // adding OrderEnoblings
        req.orderEnoblings?.forEach {
            orderCreated.addOrderEnobling(
                annotation = it.annotation,
                enobling = enoblingRepo.findById(it.enoblingId)
                    ?: throw ConversionException("${it::enoblingId.name}: ${it.enoblingId} not in database"),
                bindery = binderyRepo.findById(it.binderyId)
                    ?: throw ConversionException("${it::binderyId.name}: ${it.binderyId} not in database")
            )
        }

        orderCreated = repo.save(orderCreated)
        return RecID(
            orderCreated.orderid?.toLong()
                ?: throw ConversionException("something went wrong with saving ${OrderReq::class.simpleName} to the database")
        )
    }

    fun getOrdersForAssignee(lastAssignee: Int, authentication: Authentication) {
        // download worker for the given id and check email equality auth.name
        // download orders with assignee worker provided
        TODO("implement")
    }
}

private fun Order.toTransport(clientRepo: ClientRepo): OrderResp {
    return OrderResp(
        id = orderid ?: throw ConversionException("${::orderid.name} is null"),
        name = name,
        comment = comment,
        withdrawalDate = withdrawalDate?.toKotlinLocalDateTime(),
        completionDate = completionDate?.toKotlinLocalDateTime(),
        designsNumberForSheet = designsNumberForSheet,
        checked = checked,
        towerCut = towerCut,
        folding = folding,
        realizationDate = realizationDate.toKotlinLocalDateTime(),
        creationDate = creationDate.toKotlinLocalDateTime(),
        pages = pages,
        paperOrderTypes = paperOrderTypes.map {
            it.toTransport()
        },
        orderEnoblings = orderEnoblings.map {
            it.toTransport()
        },
        imageUrl = imageUrl?.url,
        bindery = bindery.toTransport(),
        salesman = salesman.toTransport(),
        workflowStageStops = workflowStageStops.map {
            it.toTransport()
        },
        bindingForm = bindingForm.toTransport(),
        calculationCard = calculationCard?.toTransport(),
        netSize = netSize.toTransport(),
        client = client.toTransport(clientRepo)
    )
}

private fun PaperOrderType.toTransport(): PaperOrderTypeResp {
    return PaperOrderTypeResp(
        id = papOrdTypid ?: throw ConversionException("${::papOrdTypid.name} is null"),
        grammage = grammage,
        stockCirculation = stockCirculation,
        sheetNumber = sheetNumber,
        comment = comment,
        circulation = circulation,
        platesQuantityForPrinter = platesQuantityForPrinter,
        paperType = paperType.toTransport(),
        printer = printer.toTransport(),
        colouring = colouring.toTransport(),
        impositionType = impositionType.toTransport(),
        size = size.toTransport(),
        productionSize = productionSize.toTransport(),
    )
}

private fun Colouring.toTransport(): ColouringResp {
    return ColouringResp(
        colouringId ?: throw ConversionException("${::colouringId.name} is null"),
        secondSide,
        firstSide
    )
}

private fun WorkflowStageStop.toTransport(): WorkflowStageStopResp {
    return WorkflowStageStopResp(
        wfssId = wfssId,
        comment = comment,
        assignTime = assignTime?.toKotlinLocalDateTime(),
        createTime = createTime.toKotlinLocalDateTime(),
        worker = worker?.toTransport(),
        graphEdge = graphEdge.toEmbTransport()
    )
}

private fun OrderEnobling.toTransport(): OrderEnoblingResp {
    return OrderEnoblingResp(
        id = orderEnobid ?: throw ConversionException("${::orderEnobid.name} is null"),
        annotation = annotation,
        enobling = enobling.toTransport(),
        bindery = bindery.toTransport()
    )
}

private fun CalculationCard.toTransport(): CalculationCardRespEmb {
    return CalculationCardRespEmb(
        id = calcCardId ?: throw ConversionException("${::calcCardId.name} is null"),
        transport = transport.toPlainString(),
        otherCosts = otherCosts.toPlainString(),
        enoblingCost = enoblingCost.toPlainString(),
        bindingCost = bindingCost.toPlainString(),
        printCosts = printCosts.map {
            it.toTransport()
        }
    )
}

private fun PrintCost.toTransport(): PrintCostResp {
    return PrintCostResp(
        printCostId = printCostId ?: throw ConversionException("${::printCostId.name} is null"),
        printCost = printCost.toPlainString(),
        matrixCost = matrixCost.toPlainString(),
        printer = printer.toTransport()
    )
}

private fun Size.toTransport(): SizeResp {
    return SizeResp(
        id = sizeId ?: throw ConversionException("${::sizeId.name} is null"),
        name = name,
        heigth = heigth,
        width = width
    )
}