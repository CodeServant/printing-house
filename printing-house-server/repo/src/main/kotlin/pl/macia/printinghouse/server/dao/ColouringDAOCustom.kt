package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Colouring

internal interface ColouringDAOCustom {
    fun findByPalette(firstSide: Byte, secondSide: Byte): Colouring?
}