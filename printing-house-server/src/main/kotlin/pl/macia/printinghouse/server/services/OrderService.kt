package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.OrderReq
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.*
import java.time.LocalDateTime

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

    @Autowired
    private lateinit var workerRepo: WorkerRepo

    fun findById(id: Int): OrderResp? {
        val found = repo.findById(id)
        return found?.toTransport(clientRepo)
    }

    /**
     * Creates empty [WorkflowStageStop] for this [order] and [workflowDirEdge]
     */
    private fun addEmptyWss(order: Order, workflowDirEdge: WorkflowDirEdge) {
        order.addWorkflowStageStop(
            comment = null,
            assignTime = null,
            createTime = LocalDateTime.now(),
            completionTime = null,
            worker = null,
            workflowDirEdge = workflowDirEdge
        )
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
            val first = it.colouring.firstSide
            val second = it.colouring.secondSide
            val colouring = colouringRepo.findByPalette(first, second) ?: colouringRepo.save(
                Colouring(first, second)
            )
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
                colouring = colouring,
                impositionType = impositionTypeRepo.findById(it.impositionTypeId)
                    ?: throw ConversionException("${it::impositionTypeId.name}: ${it.impositionTypeId} is not in database"),
                size = sizeRepo.createByParameters(it.size.width, it.size.heigth),
                productionSize = sizeRepo.createByParameters(it.productionSize.width, it.productionSize.heigth)
            )
        }

        fetchedGraph.startEdges.forEach {
            addEmptyWss(orderCreated, it)
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

    fun getOrdersForAssignee(lastAssignee: Int): List<OrderResp> {
        val orders = repo.findNotCompletedByLastAssignee(lastAssignee)
        return orders.toTransport(clientRepo)
    }

    /**
     * gets assigned orders (tasks) for the currently authenticated user
     * @throws AccessDeniedException when authenticated use not features in database of workers
     */
    fun getOrdersForAssignee(authentication: Authentication): List<OrderResp> {
        val worker = workerRepo.findByEmail(authentication.name) ?: throw AccessDeniedException("permission denied")
        return getOrdersForAssignee(
            worker.personId ?: throw Exception("fetched object from database does not have ${worker::personId.name}")
        )
    }

    /**
     * @param wssId [WorkflowStageStop] id
     */
    @Transactional
    fun markTaskAsDone(wssId: Int, authentication: Authentication) {
        // authorize user
        val isWorker = authentication.authorities.map { it.authority }.contains(PrimaryRoles.WORKER)
        val isWssManeger = authentication.authorities.map { it.authority }.contains(PrimaryRoles.WORKFLOW_STAGE_MANAGER)
        val workerName = authentication.name
        val order = repo.findByWssId(wssId)
            ?: throw ObjectNotFoundException("order with provided WorkflowStageStop id not found")
        val workflowStageStop = order.workflowStageStops.find {
            it.wfssId == wssId
        }!!

        fun isManagerOfWorkflow() = workflowStageStop.graphEdge.v1.workflowManagers.map {
            it.name
        }.contains(workerName)

        if (
            !(isWorker && workflowStageStop.worker?.email?.email == workerName)
            && !(isWssManeger && isManagerOfWorkflow())
        )
            throw AccessDeniedException("permission denied")

        if (!workflowStageStop.completed) { //todo probably some exception that indicates that wss is already completed
            workflowStageStop.completionTime = LocalDateTime.now()

            val previoudWss = order.workflowStageStops.filter {
                it.graphEdge.v2 == workflowStageStop.graphEdge.v2
            }

            val ifAllPrevFinished = previoudWss.all {
                it.completed
            }
            if (ifAllPrevFinished) {
                val vertOut = workflowStageStop.graphEdge.v2
                workflowStageStop.graphEdge
                    .grapf
                    .toGrapht()
                    .outgoingEdgesOf(vertOut)
                    .forEach { edge ->
                        addEmptyWss(order, edge)
                    }
            }
            repo.save(order)
        }
    }

    /**
     * Fetches not assigned [Order]s for the workflow stage of authenticated workflow stage manager.
     */
    fun fetchNotAssigned(email: String): List<OrderResp> {
        val foundWorker = workerRepo.findByEmail(email)
        val unassigned = mutableListOf<Order>()
        foundWorker?.isManagerOf?.forEach { workflowStage ->
            unassigned.addAll(
                repo.getUnassigned(
                    workflowStage.workflowStageid ?: throw RuntimeException("fetched workflow stage id is null"),
                    checked = true
                )
            )
        }
        return unassigned.map {
            it.toTransport(clientRepo)
        }
    }

    /**
     * Assign [Worker] to the latest unassigned [WorkflowStageStop] for authenticated workflow stage manager.
     * @throws NoSuchElementException when id for worker and order were not found, and criteria aren't met ([Worker] not assigned and authenticated manager have this [WorkflowStage] for keeping)
     *
     * NOTE. todo there is exception when there are 2 or more [WorkflowStageStop]s that the manager can assign to, this will probably be resolved in the future when client caches data about themselves
     */
    @Transactional
    fun assignWorker(id: Int, workerId: Int, authentication: Authentication): ChangeResp {
        val order = repo.findById(id)
        val worker = workerRepo.findById(workerId)
            ?: throw NoSuchElementException("${Worker::class.simpleName} no element in database with the given id")
        val wsStopsToAssign = order?.workflowStageStops?.filter { wss ->
            wss.graphEdge.v1.workflowManagers.map { it.email.email }.contains(authentication.name)
                    && wss.worker == null
                    && wss.completionTime == null
        }
            ?: throw NoSuchElementException("${WorkflowStageStop::class.simpleName} not found to assign this worker to")
        if (wsStopsToAssign.isEmpty()) {
            throw NoSuchElementException("${WorkflowStageStop::class.simpleName} not found to assign this worker to")
        }
        if (wsStopsToAssign.size > 1) {
            throw NoSuchElementException("more than one ${WorkflowStageStop::class.simpleName} found for such criteria")
        }
        val wssToAssign = wsStopsToAssign.first()
        wssToAssign.worker = worker
        wssToAssign.assignTime = LocalDateTime.now()
        return ChangeResp(true)
    }

    fun ordersToFinalize(name: String): List<OrderResp> {
        return repo.pathCompletedOrders(name).map { it.toTransport(clientRepo) }
    }

    @Transactional
    fun finalizeOrder(id: Int): Boolean {
        val fetched = repo.findById(id) ?: throw NoSuchElementException("there is not order for provided id")
        val isCompleted = fetched.workflowStageStops.map {
            it.completionTime
        }.filter { it == null }
            .isEmpty()
        if (!isCompleted) return false
        fetched.completionDate = LocalDateTime.now()
        return true
    }

    fun ordersToCheck(): List<OrderResp> {
    return repo.notChecked().toTransport(clientRepo)
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

private fun Collection<Order>.toTransport(clientRepo: ClientRepo): List<OrderResp> {
    return map {
        it.toTransport(clientRepo)
    }
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