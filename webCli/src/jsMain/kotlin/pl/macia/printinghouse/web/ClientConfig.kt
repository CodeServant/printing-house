package pl.macia.printinghouse.web

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

object clientConfig {
    const val domain = "localhost"
    var protocol = "http"
    var port = "8082"
    val serviceUrl = "$protocol://$domain:$port"
}

val clientDateFormat = LocalDateTime.Format {
    date(LocalDate.Formats.ISO)
    char(' ')
    hour(); char(':')
    minute()
}