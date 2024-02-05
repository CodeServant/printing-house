package pl.macia.printinghouse.web.cli

import io.kvision.*
import io.kvision.html.div
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.panel.root

class App : Application() {
    init {
        if (I18n.language !in listOf("en", "pl")) {
            I18n.language = "en"
        }
    }

    override fun start() {
        I18n.manager = DefaultI18nManager(
            mapOf(
                "en" to require("./i18n/messages-en.json"),
                "pl" to require("./i18n/messages-pl.json"),
            )
        )
        root("kvapp") {
            div("Hello world")
            // TODO
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule,
        TabulatorModule,
        TabulatorCssBootstrapModule,
        FontAwesomeModule,
        TomSelectModule,
        DatetimeModule
    )
}
