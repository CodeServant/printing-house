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
import kotlinx.serialization.KSerializer

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
 * @param onlyEdit where add button shouldn't be visible
 */
fun <T : Any> Container.insertUpdateTable(
    summaryList: List<T>,
    columnsDef: List<ColumnDefinition<T>>,
    onSelected: (T?) -> Unit,
    formPanel: () -> Component? = { null },
    onInsert: (() -> Unit)? = null,
    onUpdate: (() -> Unit)? = null,
    serializer: KSerializer<T>
) {
    val buttonType = ObservableValue(ButtonType.ADD)
    val onlyEdit = onInsert == null && onUpdate != null
    simplePanel {
        add(
            PrhTabulator(
                summaryList, options = TabulatorOptions(
                    layout = Layout.FITCOLUMNS,
                    columns = columnsDef,
                    selectableRows = 1,
                ), serializer = serializer
            ) {
                onEvent {
                    rowSelectionChangedTabulator = {
                        buttonType.value =
                            if (getSelectedRows().size == 1) ButtonType.EDIT else ButtonType.ADD
                        onSelected(getSelectedData().firstOrNull())
                    }
                }
            }
        )
        hPanel(useWrappers = true).bind(buttonType) {
            simplePanel {
                if (it == ButtonType.ADD) {
                    if (!onlyEdit) {
                        addButton {
                            onClick {
                                onInsert?.invoke()
                            }
                        }
                    }
                } else {
                    editButton {
                        onClick {
                            onUpdate?.invoke()
                        }
                    }
                }
            }
            formPanel()?.let(::add)
        }
    }
}

/**
 * This is printing house standard tabulator.
 */
class PrhTabulator<T : Any>(
    data: List<T>? = null,
    options: TabulatorOptions<T> = TabulatorOptions(),
    serializer: KSerializer<T>? = null,
    init: (PrhTabulator<T>.() -> Unit)? = null
) :
    Tabulator<T>(
        data = data,
        options = options,
        serializer = serializer
    ) {
    init {
        init?.invoke(this)
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