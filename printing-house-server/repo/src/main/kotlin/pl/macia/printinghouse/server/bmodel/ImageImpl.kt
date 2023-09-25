package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Image as PImage

internal class ImageImpl(persistent: PImage) : Image, BusinessBase<PImage>(persistent) {
    constructor(url: String, comment: String?) : this(
        PImage(url, comment)
    )

    override var impImgId: Long? by persistent::id
    override var url: String by persistent::url
    override var imageComment: String? by persistent::imageComment
}

fun Image(url: String, comment: String?): Image = ImageImpl(url, comment)