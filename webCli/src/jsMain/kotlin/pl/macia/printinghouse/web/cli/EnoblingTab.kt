package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.form.select.select
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import pl.macia.printinghouse.web.dao.EnoblingDao

enum class EnoblingSubtype {
    UV_VARNISH, PUNCH
}

@Serializable
data class Enobling(
    var name: String,
    var description: String? = null,
    var type: EnoblingSubtype? = null
)

class EnoblingTab : SimplePanel() {
    init {
        val enoblingContext = ObservableValue<Enobling?>(null)

        insertUpdateTable(
            summaryList = listOf(
                Enobling(
                    "muśnięcie UV",
                    "to najlepsza z form papiery, najbezpieczniejsza dla dzieci",
                    type = EnoblingSubtype.UV_VARNISH
                ),
                Enobling("papierowy wykrojnik", type = EnoblingSubtype.PUNCH),
                Enobling("karton uszlachetniony", "karton ten jest nalepszym kartonem ble bla blu"),
                Enobling("papier błysk"),
            ),
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Type", "type"),
            ),
            onSelected = {
                enoblingContext.value = it //todo every time i create the same value maby move it somewhere else
            },
            formPanel = {
                SimplePanel {
                    val nameInp = textInput("Name")
                    val descriptionInp = textInput("Description (optional)")
                    select {
                        options = listOf(
                            Pair(EnoblingSubtype.UV_VARNISH.toString(), "UV Varnish"),
                            Pair(EnoblingSubtype.PUNCH.toString(), "Punch")
                        )
                        enoblingContext.subscribe {
                            value = it?.type.toString()
                        }
                    }
                    enoblingContext.subscribe {
                        nameInp.value = it?.name
                        descriptionInp.value = it?.description
                    }
                }
            }, serializer = serializer()
        )
    }
}

class EnoblingSelect : TomSelect(label = "Enobling") {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callbacks ->
                EnoblingDao().allEnoblings(
                    onFulfilled = { enobs ->
                        callbacks(
                            enobs.map {
                                obj {
                                    this.text = it.name
                                    this.value = it.id
                                }
                            }.toTypedArray()
                        )
                    },
                    onRejected = {
                        TODO("implement EnoblingSelect onRejected")
                    }
                )
            }, shouldLoad = { false }
        )
        tsOptions = TomSelectOptions(
            preload = true
        )
    }
}