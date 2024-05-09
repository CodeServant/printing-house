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
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.PaperTypeReq
import pl.macia.printinghouse.response.PaperTypeResp
import pl.macia.printinghouse.web.dao.PaperTypeDao

@Serializable
data class PaperTypeSummary(
    var id: Int,
    var name: String,
    var shortName: String,
)

class PaperTypeTab(papTypeResponses: List<PaperTypeResp>) : SimplePanel() {
    val summaryList = ObservableListWrapper(papTypeResponses.map {
        PaperTypeSummary(it.id, it.name, it.shortName)
    }.toMutableList())

    init {
        val textFormContent = ObservableValue<PaperTypeSummary?>(null)
        val form = FormPanel<PaperTypeSummary>()
        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Short Name", "shortName"),
            ),
            onSelected = {
                textFormContent.value = it //todo every time i create the same value maby move it somewhere else
            },
            formPanel = {
                SimplePanel {
                    val name = TextInput("Name")
                    val shortName = TextInput("Short Name")
                    form.add(PaperTypeSummary::name, name, required = true)
                    form.add(PaperTypeSummary::shortName, shortName, required = true)
                    this.add(form)
                    textFormContent.subscribe {
                        name.value = it?.name
                        shortName.value = it?.shortName
                    }
                }
            }, onInsert = {
                if (form.validate(true)) {
                    val insertedName = form[PaperTypeSummary::name]
                        ?: throw RuntimeException("validation don't work correctly ${PaperTypeSummary::name.name} should not be null")
                    val insertedShortName = form[PaperTypeSummary::shortName]
                        ?: throw RuntimeException("validation don't work correctly ${PaperTypeSummary::name.name} should not be null")
                    val req = PaperTypeReq(
                        name = insertedName,
                        shortName = insertedShortName,
                    )
                    PaperTypeDao().newPaperTypeReq(
                        req,
                        onFulfilled = { recID ->
                            summaryList.add(PaperTypeSummary(recID.id.toInt(), insertedName, insertedShortName))
                        },
                        onRejected = {
                            TODO("on rejected when inserting new PaperType by manager")
                        },
                    )
                }

            }
        )
    }
}

/**
 * Selection panel for paper type.
 */
class SelectPaperType(label: String? = null) : TomSelect(label = label) {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                PaperTypeDao().allPaperTypes(
                    onFulfilled = {
                        callback(
                            it.map {
                                obj {
                                    this.text = it.name
                                    this.value = it.id
                                }
                            }.toTypedArray()
                        )
                    },
                    onRejected = {
                        TODO("implement paper type fetch all rejected")
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