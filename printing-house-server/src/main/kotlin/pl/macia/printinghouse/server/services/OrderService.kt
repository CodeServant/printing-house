package pl.macia.printinghouse.server.services

import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.ClientRepo
import pl.macia.printinghouse.server.repository.OrderRepo

@Service
class OrderService {
    @Autowired
    private lateinit var repo: OrderRepo

    @Autowired
    private lateinit var clientRepo: ClientRepo
    fun findById(id: Int): OrderResp? {
        val found = repo.findById(id)
        return found?.toTransport(clientRepo)
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