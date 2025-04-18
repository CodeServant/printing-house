package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Size

internal interface SizeDAO : JpaRepository<Size, Int>, SizeDAOCustom {
    fun findByName(s: String): Size?
    fun findByNameIsNotNull(): List<Size>
}