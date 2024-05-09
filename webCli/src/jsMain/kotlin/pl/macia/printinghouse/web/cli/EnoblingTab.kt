package pl.macia.printinghouse.web.cli

import io.kvision.core.BsBgColor
import io.kvision.core.BsColor
import io.kvision.form.FormPanel
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.form.select.select
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.toast.ToastContainer
import io.kvision.toast.ToastContainerPosition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.*
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

data class EnoblingData(
    val name: String,
    val description: String?,
    val type: String?
)

class EnoblingTab(enobligs: List<EnoblingResp>) : SimplePanel() {
    val form = FormPanel<EnoblingData>()

    init {
        val pickedEnobling = ObservableValue<Enobling?>(null)
        val enoblingsTabList = ObservableListWrapper<Enobling>()
        enoblingsTabList.addAll(
            enobligs.map {
                val type = when (it) {
                    is PunchResp -> EnoblingSubtype.PUNCH
                    is UVVarnishResp -> EnoblingSubtype.UV_VARNISH
                    else -> null
                }
                Enobling(it.id, it.name, it.description, type)
            }
        )
        insertUpdateTable(
            summaryList = enoblingsTabList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Type", "type"),
            ),
            onSelected = {
                pickedEnobling.value = it
            },
            formPanel = {
                SimplePanel {
                    val nameInp = TextInput("Name")
                    val descriptionInp = TextInput("Description (optional)")
                    form.add(EnoblingData::name, nameInp, required = true)
                    form.add(EnoblingData::description, descriptionInp, required = false)
                    form.add(
                        EnoblingData::type,
                        select(floating = true, label = "enobling type") {
                            options = listOf(
                                Pair(EnoblingSubtype.UV_VARNISH.toString(), "UV Varnish"),
                                Pair(EnoblingSubtype.PUNCH.toString(), "Punch")
                            )
                            pickedEnobling.subscribe {
                                value = it?.type.toString()
                            }
                        }, required = false
                    )
                    pickedEnobling.subscribe {
                        nameInp.value = it?.name
                        descriptionInp.value = it?.description
                    }
                    this.add(form)
                }
            },
            onUpdate = {
                val picked = pickedEnobling.value
                if (picked != null && form.validate(true)) {
                    val name = form[EnoblingData::name]
                        ?: throw RuntimeException("validation should require property ${EnoblingData::name}")
                    val descr = form[EnoblingData::description]
                    val changereq = when (picked.type) {
                        EnoblingSubtype.PUNCH -> PunchChangeReq(name, descr, true)
                        EnoblingSubtype.UV_VARNISH -> UVVarnishChangeReq(name, descr, true)
                        else -> EnoblingChangeReq(name, descr, true)
                    }

                    EnoblingDao().changeEnobling(
                        picked.id,
                        changereq,
                        onFulfilled = {
                            val toastContainer = ToastContainer(ToastContainerPosition.BOTTOMCENTER)
                            toastContainer.showToast(
                                "record updated succesfully",
                                "record update",
                                color = BsColor.SUCCESS,
                                bgColor = BsBgColor.SUCCESSSUBTLE,
                                autohide = true,
                                animation = true,
                                delay = 3000
                            )
                            val pickedIndex = enoblingsTabList.indexOf(picked)
                            enoblingsTabList[pickedIndex] = picked.copy(name = name, description = descr)
                        },
                        onRejected = {
                            TODO("on rejected when updating enobling data")
                        },
                    )

                }
            },
            onInsert = {
                if (form.validate(true)) {
                    val name = form[EnoblingData::name]
                        ?: throw RuntimeException("validation should require property ${EnoblingData::name}")
                    val descr = form[EnoblingData::description]
                    val type = if (form[EnoblingData::type] == null) null
                    else
                        try {
                            EnoblingSubtype.valueOf(form[EnoblingData::type]!!)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    val enobReq = when (type) {
                        EnoblingSubtype.UV_VARNISH -> {
                            UVVarnishReq(name, descr)
                        }

                        EnoblingSubtype.PUNCH -> {
                            PunchReq(name, descr)
                        }

                        else -> {
                            EnoblingReq(name, descr)
                        }
                    }
                    EnoblingDao().newEnoblingReq(
                        enobReq,
                        onFulfilled = {
                            val toastContainer = ToastContainer(ToastContainerPosition.BOTTOMCENTER)
                            toastContainer.showToast(
                                "record added succesfully",
                                "record added",
                                color = BsColor.SUCCESS,
                                bgColor = BsBgColor.SUCCESSSUBTLE,
                                autohide = true,
                                animation = true,
                                delay = 3000
                            )
                            val nextId = it.id.toInt()
                            enoblingsTabList.add(
                                Enobling(
                                    nextId, name, descr, type
                                )
                            )
                        },
                        onRejected = {
                            TODO("on rejected when enobling not inserted properly")
                        }
                    )
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