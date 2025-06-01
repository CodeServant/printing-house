package pl.macia.printinghouse.web.cli

import io.kvision.*
import io.kvision.core.FlexDirection
import io.kvision.core.onClickLaunch
import io.kvision.form.formPanel
import io.kvision.form.text.Password
import io.kvision.html.button
import io.kvision.html.h1
import io.kvision.html.link
import io.kvision.html.p
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.flexPanel
import io.kvision.panel.root
import io.kvision.routing.Routing
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.utils.rem
import kotlinx.browser.document
import kotlinx.browser.localStorage
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.dao.LoginDao
import pl.macia.printinghouse.web.logged

enum class Screens(val path: String) {
    LOGIN("login"), MENU("menu"), MAIN("main")
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
        fun rut(picked: Screens) {
            routing.on(picked.path, { _ ->
                screen.value = if (screen.value != picked)
                    picked
                else null
            })
        }
        rut(Screens.LOGIN)
        rut(Screens.MENU)
        rut(Screens.MAIN)

        routing.on({ _ ->
            screen.value = null
        })
        root("kvapp").bind(screen) {
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

                Screens.MAIN -> {
                    add(MainScreen {
                        routing.navigate(Screens.LOGIN.path)
                    })
                }

                null -> {
                    if (justStarted)
                        routing.navigate("")
                    else
                        if (storage.logged()) routing.navigate(Screens.MENU.path)
                        else routing.navigate(Screens.MAIN.path)
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

class MainScreen(signInClicked: () -> Unit) : SimplePanel() {
    init {
        val h1Size = 1.15.rem
        document.title =
            "The System for Monitoring and Assisting with Workflow Processes within a Small Printing House."
        button(text = "Sign In") {
            onClick {
                signInClicked()
            }
            marginBottom = 2.rem
        }
        h1 {
            fontSize = h1Size
            +"Project Name"
        }
        p {
            +"${document.title}"
        }
        h1 {
            fontSize = h1Size
            +"Author"
        }
        p {
            +"Maciej Ostrowski"
        }
        h1 {
            fontSize = h1Size
            +"Version"
        }
        p {
            +"0.0.1"
        }
        h1 {
            fontSize = h1Size
            +"About This Project"
        }
        p {
            +"This is internal platform for employees of the small printing house. It consists of 3 services: Server that exposes REST API, "
            +"Web Client (this) and MySQL database."
        }
        p {
            +"This project lets manager to insert some data related to the business domain, Salesman to insert Orders and Workers to"
            +" to report on work done. The Manager inserts a Workflow Graph that defines the Workflow route. Next, Salesman inserts"
            +" an Order that needs to be accepted by the Manager. After that, the Order is going to be executed by Workers. The Workflow Manager"
            +" picks the proper Worker for the job and when the Stage is completed, the Worker confirms the task done."
            +" The system then automatically moves tasks to next Workflow Stage until the entire Order is done."
        }
        h1 {
            fontSize = h1Size
            +"Tech Stack"
        }
        p {
            +"Server is build with Spring Boot and Kotlin, whereas Web Client is build with KVision."
            flexPanel(direction = FlexDirection.COLUMN) {
                link(label = "KVision", url = "https://kvision.io/")
                link(label = "Spring Boot", url = "https://spring.io/projects/spring-boot")
                link(label = "MySQL", url = "https://www.mysql.com/")
                link(label = "Kotlin Language", url = "https://kotlinlang.org/")
                link(label = "Hibernate", url = "https://hibernate.org/")
            }
        }
        h1 {
            fontSize = h1Size
            +"Source Code"
        }
        link("GitHub", "https://github.com/CodeServant/printing-house")
    }
}