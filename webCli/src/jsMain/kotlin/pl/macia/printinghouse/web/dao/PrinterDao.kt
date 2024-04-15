package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.request.PrinterChangeReq
import pl.macia.printinghouse.request.PrinterReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.clientConfig

class PrinterDao {
    private val url = "${clientConfig.serviceUrl}/api/printers"
    private val dullDao = DullDao(url)
    fun allPrinters(onFulfilled: (List<PrinterResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newPrinterReq(
        printerReq: PrinterReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(printerReq, onFulfilled, onRejected)

    fun getPrinter(
        id: Int,
        onFulfilled: (PrinterResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun changePrinter(
        id: Int,
        changeReq: PrinterChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}