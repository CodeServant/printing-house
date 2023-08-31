package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Colouring

internal interface ColouringDAO : JpaRepository<Colouring, Byte>, ColouringDAOCustom