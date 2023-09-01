package pl.macia.printinghouse.server.bmodel

sealed interface Colouring {
    var colouringId: Short?
    var secondSide: Byte
    var firstSide: Byte
}