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

fun Container.cancelButton(onClick: () -> Unit) {
    button("calcel", style = ButtonStyle.DANGER) {
        onClick {
            onClick()
        }
    }
}

fun Container.doubleInputField(label: String, init: (Container.() -> Unit)? = null): Numeric {
    return numeric(label = label) {
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
 * Stabdard text input field.
 */
fun Container.textInput(label: String, init: (Text.() -> Unit)? = null): TextInput {
    return TextInput(label, init = init).apply(::add)
}

class TextInput(label: String, init: (Text.() -> Unit)? = null) : Text(label = label, floating = true, init = init)

/**
 * Standard table for editing and adding elements
 * @param onSelected what happens when specific item is selected
 * @param formPanel is the panel that is supposed to apear in the same window as table for fast add and eddit option
 */
inline fun <reified T : Any> Container.insertUpdateTable(
    summaryList: List<T>,
    columnsDef: List<ColumnDefinition<T>>,
    crossinline onSelected: (T?) -> Unit,
    crossinline formPanel: () -> Component? = { null },
    crossinline onInsert: (() -> Unit) = { },
    crossinline onUpdate: (() -> Unit) = { }
) {
    val buttonType = ObservableValue(ButtonType.ADD)

    simplePanel {
        tabulator(
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
                    onSelected(getSelectedData().firstOrNull())
                }
            }
        }
        hPanel(useWrappers = true).bind(buttonType) {
            simplePanel {
                if (it == ButtonType.ADD)
                    addButton {
                        onClick {
                            onInsert()
                        }
                    }
                else {
                    editButton {
                        onClick {
                            onUpdate()
                        }
                    }
                }
            }
            formPanel()?.let(::add)
        }
    }
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