package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.number.Numeric
import io.kvision.form.number.numeric
import io.kvision.form.text.Text
import io.kvision.html.*
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.*
import io.kvision.toast.ToastContainer
import io.kvision.toast.ToastContainerPosition

enum class ButtonType {
    ADD, EDIT
}

class AddButton(init: (AddButton.() -> Unit)? = null) : Button("dodaj") {
    init {
        background = Background(
            color = Color.name(Col.FORESTGREEN)
        )
        init?.invoke(this)
    }
}

class EditButton(init: (EditButton.() -> Unit)? = null) : Button("edit") {
    init {

        init?.invoke(this)
    }
}

class DetailsButton(text: String = "details", init: (DetailsButton.() -> Unit)? = null) : Button(text = text) {
    init {

        init?.invoke(this)
    }
}

class CancelButton(
    text: String = "calcel",
    style: ButtonStyle = ButtonStyle.DANGER,
    init: (CancelButton.() -> Unit)? = null
) : Button(text = text, style = style) {
    init {
        init?.invoke(this)
    }
}

fun Container.addButton(init: (AddButton.() -> Unit)? = null): AddButton {
    return AddButton(init).apply(::add)
}

fun Container.editButton(init: (EditButton.() -> Unit)? = null): EditButton {
    return EditButton(init).apply(::add)
}

fun Container.acceptButton(init: (Button.() -> Unit)? = null) {
    button("accept", style = ButtonStyle.SUCCESS, init = init)
}

fun Container.detailsButton(text: String = "details", init: (DetailsButton.() -> Unit)? = null) {
    add(DetailsButton(text, init))
}

fun Container.cancelButton(
    text: String = "calcel",
    style: ButtonStyle = ButtonStyle.DANGER,
    init: (CancelButton.() -> Unit)? = null
) {
    add(CancelButton(text, style, init))
}

fun Container.doubleInputField(label: String, init: (Container.() -> Unit)? = null): Numeric {
    return numeric(label = label) {
        init?.invoke(this)
    }
}

class DoubleInputField(label: String, init: (Container.() -> Unit)? = null) : Numeric(label = label) {
    init {
        init?.invoke(this)
    }
}

class IntegerInput(label: String?, init: (IntegerInput.() -> Unit)? = null) :
    Numeric(decimals = 0, rich = true, label = label) {
    init {
        init?.invoke(this)
    }
}

/**
 * Standard text input field.
 */
fun Container.textInput(label: String, init: (Text.() -> Unit)? = null): TextInput {
    return TextInput(label, init = init).apply(::add)
}

class TextInput(label: String, init: (Text.() -> Unit)? = null) : Text(label = label, floating = true, init = init)

/**
 * Standard table for editing and adding elements. It can be also used as table to chose data from.
 * @param onSelected what happens when specific item is selected
 * @param formPanel is the panel that is supposed to apear in the same window as table for fast add and eddit option
 */
inline fun <reified T : Any> Container.insertUpdateTable(
    summaryList: List<T>,
    columnsDef: List<ColumnDefinition<T>>,
    crossinline onSelected: (T?) -> Unit,
    crossinline formPanel: () -> Component? = { null },
    noinline onInsert: (() -> Unit)? = null,
    noinline onUpdate: (() -> Unit)? = null,
    editButtonText: String? = null
) {
    val buttonType = ObservableValue(ButtonType.ADD)
    var isSelected = false
    simplePanel {
        add(
            prhTabulator(
                summaryList, options = TabulatorOptions(
                    layout = Layout.FITCOLUMNS,
                    columns = columnsDef,
                    selectableRows = 1,
                )
            ) {
                onEvent {
                    rowSelectionChangedTabulator = {
                        buttonType.value =
                            if (getSelectedRows().size == 1) ButtonType.EDIT else ButtonType.ADD
                        isSelected = getSelectedData().isNotEmpty()
                        onSelected(getSelectedData().firstOrNull())
                    }
                }
            }
        )
        hPanel(useWrappers = true).bind(buttonType) {
            simplePanel {
                if (it == ButtonType.ADD) {
                    if (onInsert != null) {
                        addButton {
                            onClick {
                                onInsert.invoke()
                            }
                        }
                    }
                } else {
                    if (onUpdate != null) {
                        editButton {
                            if (editButtonText != null)
                                text = editButtonText
                            onClick {
                                onUpdate.invoke()
                            }
                        }
                    }
                }
            }
            if (onInsert != null || onUpdate != null && isSelected)
                formPanel()?.let(::add)
        }
    }
}

/**
 * This is printing house standard tabulator.
 */
inline fun <reified T : Any> prhTabulator(
    data: List<T>? = null,
    options: TabulatorOptions<T> = TabulatorOptions(),
    noinline init: (Tabulator<T>.() -> Unit)? = null
): Tabulator<T> {
    val tab = Tabulator.create(
        data,
        options = options,
    )
    init?.invoke(tab)
    return tab
}

class LoaderSpinner(var type: SpinnerType = SpinnerType.PRIMARY) : SimplePanel() {
    enum class SpinnerType(val cssClassName: String) {
        PRIMARY("text-primary"),
        SECONDARY("text-secondary"),
        SUCCESS("text-success"),
        DANGER("text-danger"),
        WARRNING("text-warning"),
        INFO("text-info"),
        LIGHT("text-light"),
        DARK("text-dark")
    }

    init {
        div(className = "spinner-border ${type.cssClassName}") {
            span(className = "visually-hidden") {
                role = "status"
            }
        }
    }
}

private fun crudToast(message: String, title: String) {
    val toastContainer = ToastContainer(ToastContainerPosition.BOTTOMCENTER)
    toastContainer.showToast(
        message,
        title,
        color = BsColor.SUCCESS,
        bgColor = BsBgColor.SUCCESSSUBTLE,
        autohide = true,
        animation = true,
        delay = 3000
    )
}

fun insertToast(message: String, title: String = "record inserted") = crudToast(message, title)

fun updateToast(message: String, title: String = "record updated") = crudToast(message, title)

fun failToast(message: String, title: String) {
    val toastContainer = ToastContainer(ToastContainerPosition.BOTTOMCENTER)
    toastContainer.showToast(
        message,
        title,
        color = BsColor.DANGER,
        bgColor = BsBgColor.DANGERSUBTLE,
        autohide = true,
        animation = true,
        delay = 3000
    )
}