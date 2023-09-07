package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.URL as PURL

internal class URLImpl(persistent: PURL) : URL, BusinessBase<PURL>(persistent) {
    constructor(url: String) : this(
        PURL(url)
    )

    override var urlId: Long? by persistent::id
    override var url: String by persistent::url
}

fun URL(url: String): URL = URLImpl(url)