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
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.response.PunchResp
import pl.macia.printinghouse.response.UVVarnishResp
import pl.macia.printinghouse.web.dao.EnoblingDao

enum class EnoblingSubtype {
    UV_VARNISH, PUNCH
}

@Serializable
data class Enobling(
    var id: Int,
    var name: String,
    var description: String? = null,
    var type: EnoblingSubtype? = null
)

class EnoblingTab(enobligs: List<EnoblingResp>) : SimplePanel() {
    init {
        val pickedEnobling = ObservableValue<Enobling?>(null)

        insertUpdateTable(
            summaryList = enobligs.map {
                val type = when (it) {
                    is PunchResp -> EnoblingSubtype.PUNCH
                    is UVVarnishResp -> EnoblingSubtype.UV_VARNISH
                    else -> null
                }
                Enobling(it.id, it.name, it.description, type)
            },
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Type", "type"),
            ),
            onSelected = {
                pickedEnobling.value = it
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
                        pickedEnobling.subscribe {
                            value = it?.type.toString()
                        }
                    }
                    pickedEnobling.subscribe {
                        nameInp.value = it?.name
                        descriptionInp.value = it?.description
                    }
                }
            }
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