package pl.macia.printinghouse.server.bmodel

sealed interface Colouring {
    var colouringId: Byte?
    var secondSide: Byte
    var firstSide: Byte
}