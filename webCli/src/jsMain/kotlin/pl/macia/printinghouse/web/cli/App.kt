package pl.macia.printinghouse.web.cli

import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.form.formPanel
import io.kvision.form.text.Password
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.root
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.web.dao.LoginDao

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

class SignInForm : SimplePanel() {
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
        acceptButton() {
            onClickLaunch {
                if (logForm.validate()) {
                    LoginDao().signIn(logForm[LoginReq::login]!!, logForm[LoginReq::password]!!) {
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