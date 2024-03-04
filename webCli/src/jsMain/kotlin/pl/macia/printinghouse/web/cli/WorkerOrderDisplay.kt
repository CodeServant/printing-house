package pl.macia.printinghouse.web.cli

import io.kvision.html.button
import io.kvision.html.link
import io.kvision.html.p
import io.kvision.panel.SimplePanel
import pl.macia.printinghouse.response.*

class WorkerOrderDisplay(orderResp: OrderResp, onBack: () -> Unit, onDone: () -> Unit) : SimplePanel() {
    init {
        add(DisplayOrder(orderResp))
        button("confirm done") {
            onClick {
                onDone()
                onBack()
            }
        }
        button("cancel") {
            onClick {
                onBack()
            }
        }
    }
}

private class DisplayOrder(orderResp: OrderResp) : SimplePanel() {
    init {
        p {
            +orderResp.name
        }
        p {
            +(orderResp.comment ?: "")
        }
        p {
            +"withdrawalDate: ${(orderResp.withdrawalDate?.toString()) ?: "no date"}"
        }
        p {
            +"withdrawalDate: ${(orderResp.completionDate?.toString()) ?: "no date"}"
        }
        p {
            +"designsNumberForSheet: ${orderResp.designsNumberForSheet}"
        }
        p {
            +"checked: ${orderResp.checked}"
        }
        p {
            +"towerCut: ${orderResp.towerCut}"
        }
        p {
            +"folding: ${orderResp.folding}"
        }
        p {
            +"realizationDate: ${orderResp.realizationDate}"
        }
        p {
            +"creationDate: ${orderResp.creationDate}"
        }
        p {
            +"pages: ${orderResp.pages}"
        }

        p {
            +"pages: ${orderResp.pages}"
        }

        p {
            +"paperOrderTypes"
            orderResp.paperOrderTypes.forEach {
                add(PaperOrderTypeInfo(it))
            }
        }
        p {
            +"orderEnoblings"
            orderResp.orderEnoblings.forEach {
                add(OrderEnoblingInfo(it))
            }
        }
        if (orderResp.imageUrl != null)
            link(url = "${orderResp.imageUrl}", label = "drawing", target = "_blank")

        add(BinderyIInfo(orderResp.bindery, label = "bindery"))
        add(SalesmanInfo(orderResp.salesman, label = "salesman"))
        add(BindingFormInfo(bindingFormResp = orderResp.bindingForm, label = "binding form"))
        add(SizeInfo(orderResp.netSize, label = "netSize"))
        add(ClientInfo(orderResp.client, label = "client"))
    }
}

private class BinderyIInfo(bindery: BinderyResp, label: String) : SimplePanel() {
    init {
        p {
            +"$label: ${bindery.name}"
        }
    }
}

private class BindingFormInfo(bindingFormResp: BindingFormResp, label: String) : SimplePanel() {
    init {
        p {
            +"${label}: ${bindingFormResp.name}"
        }
    }
}

private class PaperOrderTypeInfo(paperOrderTypeResp: PaperOrderTypeResp) : SimplePanel() {
    init {
        p { +"grammage ${paperOrderTypeResp.grammage}" }
        p { +"stockCirculation ${paperOrderTypeResp.stockCirculation}" }
        p { +"sheetNumber ${paperOrderTypeResp.sheetNumber}" }
        p { +"comment: ${(paperOrderTypeResp.comment ?: "")}" }
        p { +"circulation ${paperOrderTypeResp.circulation}" }
        p { +"platesQuantityForPrinter ${paperOrderTypeResp.platesQuantityForPrinter}" }
        p { +"paper type ${paperOrderTypeResp.paperType.shortName}" }
        p { +"printer ${paperOrderTypeResp.printer.digest}" }
        p { +"colouring ${paperOrderTypeResp.colouring.firstSide}/${paperOrderTypeResp.colouring.secondSide}" }
        p { +"imposition type ${paperOrderTypeResp.impositionType.name}" }
        add(SizeInfo(paperOrderTypeResp.size, label = "size"))
        add(SizeInfo(paperOrderTypeResp.productionSize, label = "productionSize"))
    }
}

private class OrderEnoblingInfo(orderEnoblingResp: OrderEnoblingResp) : SimplePanel() {
    init {
        add(BinderyIInfo(orderEnoblingResp.bindery, label = "bindery"))
        add(EnoblingInfo(orderEnoblingResp.enobling, label = "enobling"))
        p {
            +"${orderEnoblingResp.annotation}"
        }
    }
}

private class SizeInfo(size: SizeResp, label: String) : SimplePanel() {
    init {
        if (size.name != null) {
            p {
                +"$label: ${size.name}"
            }
        } else {
            p {
                +"$label: ${size.width}x${size.heigth}"
            }
        }

    }
}

private class SalesmanInfo(salesman: SalesmanResp, label: String) : SimplePanel() {
    init {
        p {
            +"${label}: ${salesman.name} ${salesman.surname}"
        }
    }
}

private class ClientInfo(clientResp: ClientResp, label: String) : SimplePanel() {
    init {
        val name = when (clientResp) {
            is CompanyClientResp -> clientResp.name
            is IndividualClientResp -> "${clientResp.name} ${clientResp.surname}"
        }
        p {
            +"$label: $name"
        }
    }
}

private class EnoblingInfo(enoblingResp: EnoblingResp, label: String) : SimplePanel() {
    init {
        p {
            +"$label: ${enoblingResp.name}"
        }
    }
}