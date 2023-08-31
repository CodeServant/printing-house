package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.ImpositionType

internal interface ImpositionTypeDAO : JpaRepository<ImpositionType, Int>, ImpositionTypeDAOCustom {
    fun findByName(name: String): ImpositionType?
}