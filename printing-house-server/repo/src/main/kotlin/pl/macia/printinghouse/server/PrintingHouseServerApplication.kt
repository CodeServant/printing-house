package pl.macia.printinghouse.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class PrintingHouseServerApplication

internal fun main(args: Array<String>) {
    runApplication<PrintingHouseServerApplication>(*args)
}
