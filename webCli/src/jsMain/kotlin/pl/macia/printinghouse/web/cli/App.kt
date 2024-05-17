package pl.macia.printinghouse.web.cli

import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.form.formPanel
import io.kvision.form.text.Password
import io.kvision.html.p
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.root
import io.kvision.routing.Routing
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlinx.browser.localStorage
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.dao.LoginDao
import pl.macia.printinghouse.web.logged

enum class Screens(val path: String) {
    LOGIN("login"), MENU("menu")
}

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
        val storage = StorageInfo(localStorage)
        val screen = ObservableValue<Screens?>(null)
        val routing = Routing.init()
        var justStarted = true
        root("kvapp").bind(screen) {
            routing.on(Screens.LOGIN.path, { _ ->
                screen.value = if (screen.value != Screens.LOGIN)
                    Screens.LOGIN
                else null
            })
            routing.on(Screens.MENU.path, { _ ->
                screen.value = if (screen.value != Screens.MENU)
                    Screens.MENU
                else null
            })
            routing.on({ _ ->
                screen.value = null
            })
            when (it) {
                Screens.MENU -> {
                    if (storage.logged()) {
                        add(Menu())
                    }
                }

                Screens.LOGIN -> {
                    add(SignInForm {
                        routing.navigate(Screens.MENU.path)

                    })
                }

                null -> {
                    if (justStarted)
                        routing.navigate("")
                    else
                        if (storage.logged()) routing.navigate(Screens.MENU.path)
                        else routing.navigate(Screens.LOGIN.path)
                    justStarted = false
                }
            }
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

class SignInForm(onSignIn: () -> Unit) : SimplePanel() {
    init {
        val message = ObservableValue<String?>(null)
        val logForm = formPanel<LoginReq> {
            val log = textInput(tr("login"))
            add(
                LoginReq::login,
                log,
                required = true,
                requiredMessage = tr("the field is required")
            )
            val pass = Password(label = tr("password"), floating = true)
            add(
                LoginReq::password,
                pass,
                required = true,
                requiredMessage = tr("the field is required")
            )
        }
        acceptButton {
            onClickLaunch {
                if (logForm.validate()) {
                    LoginDao().signIn(logForm[LoginReq::login]!!, logForm[LoginReq::password]!!, onSignIn) {
                        message.value = tr("wrong credentials")
                    }
                }
            }
        }
        p().bind(message) {
            content = it
        }
    }
}