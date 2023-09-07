package pl.macia.printinghouse.server.bmodel

sealed interface Size {
    var sizeId: Int?
    var name: String?
    var heigth: Double
    var weigth: Double
}