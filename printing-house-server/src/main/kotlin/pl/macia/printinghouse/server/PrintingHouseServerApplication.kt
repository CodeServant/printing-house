package pl.macia.printinghouse.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PrintingHouseServerApplication

//todo add some controller to edit colourings

fun main(args: Array<String>) {
    runApplication<PrintingHouseServerApplication>(*args)
}
