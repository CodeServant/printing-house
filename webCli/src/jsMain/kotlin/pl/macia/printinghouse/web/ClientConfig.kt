package pl.macia.printinghouse.web

object clientConfig {
    const val domain = "localhost"
    var protocol = "http"
    var port = "8080"
    val serviceUrl = "$protocol://$domain:$port"
}