package pl.macia.printinghouse.server.bmodel

sealed interface Image {
    var impImgId: Long?
    var url: String
    var imageComment: String?
}