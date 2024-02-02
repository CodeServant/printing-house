package pl.macia.printinghouse.web.cli

import io.kvision.*
import io.kvision.html.div
import io.kvision.panel.root

class App : Application() {
    override fun start() {
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
