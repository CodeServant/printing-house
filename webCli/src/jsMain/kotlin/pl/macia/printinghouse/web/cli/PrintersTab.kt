package pl.macia.printinghouse.web.cli

import io.kvision.form.FormPanel
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import pl.macia.printinghouse.request.PrinterChangeReq
import pl.macia.printinghouse.request.PrinterReq
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.web.dao.PrinterDao

class PrintersTab(printers: List<PrinterResp>) : SimplePanel() {
    private val printerTable = ObservableListWrapper(printers.toMutableList())

    init {
        val printerTextFormData = ObservableValue<PrinterResp?>(null)
        val form = FormPanel<PrinterResp>()
        insertUpdateTable(
            summaryList = printerTable,
            columnsDef = listOf(
                ColumnDefinition("Digest", "digest"),
                ColumnDefinition("Name", "name"),
            ),
            onSelected = {
                printerTextFormData.value = it
            },
            formPanel = {
                SimplePanel {
                    val digest = TextInput("Digest")
                    val name = TextInput("Name")
                    form.add(PrinterResp::digest, digest, required = true)
                    form.add(PrinterResp::name, name, required = true)
                    printerTextFormData.subscribe {
                        digest.value = it?.digest
                        name.value = it?.name
                    }
                    this.add(form)
                }
            },
            onInsert = {
                if (form.validate(true)) {
                    val insertedName = form[PrinterResp::name]
                        ?: throw RuntimeException("validation should not pass ${PrinterResp::name.name} as empty")
                    val insertedDigest = form[PrinterResp::digest]
                        ?: throw RuntimeException("validation should not pass ${PrinterResp::digest.name} as empty")
                    PrinterDao().newPrinterReq(
                        PrinterReq(insertedName, insertedDigest),
                        onFulfilled = {
                            printerTable.add(
                                PrinterResp(
                                    it.id.toInt(),
                                    insertedName,
                                    insertedDigest
                                )
                            )
                        },
                        onRejected = {
                            TODO("on rejected when not correctly insert printer by manager")
                        },
                    )
                }
            },
            onUpdate = {
                if (form.validate(true) && printerTextFormData.value != null) {
                    val insertedName = form[PrinterResp::name]
                        ?: throw RuntimeException("validation should not pass ${PrinterResp::name.name} as empty")
                    val insertedDigest = form[PrinterResp::digest]
                        ?: throw RuntimeException("validation should not pass ${PrinterResp::digest.name} as empty")
                    val pickedId = printerTextFormData.value?.id
                        ?: throw RuntimeException("id field not existing in picked Printer")
                    PrinterDao().changePrinter(
                        pickedId,
                        PrinterChangeReq(insertedName, insertedDigest),
                        onFulfilled = {
                            val pickedPrinte = printerTable.indexOf(printerTextFormData.value)
                            printerTable[pickedPrinte] = PrinterResp(
                                pickedId,
                                insertedName,
                                insertedDigest
                            )
                        }, onRejected = {
                            TODO("on rejected when not correctly updated printer by manager")
                        }
                    )
                }
            }
        )
    }
}

class SelectPrinter(label: String? = null) : TomSelect(label = label) {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                PrinterDao().allPrinters(
                    onFulfilled = { fetched ->
                        val arr = fetched.map { printer ->
                            obj {
                                this.text = printer.name
                                this.value = printer.id
                            }
                        }.toTypedArray()
                        callback(arr)
                    },
                    onRejected = {
                        TODO("implement on rejected fetch in printers")
                    },
                )
            },
            shouldLoad = { false }
        )
        tsOptions = TomSelectOptions(
            preload = true
        )
    }
}