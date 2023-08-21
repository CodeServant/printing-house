package pl.macia.printinghouse.server.dao

import pl.macia.printinghouse.server.dto.Size

interface SizeDAOCustom {
    fun findOrCreate(width: Double, height: Double): Size
}