package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Size

internal interface SizeDAOCustom {
    fun findOrCreate(width: Double, height: Double): Size
}