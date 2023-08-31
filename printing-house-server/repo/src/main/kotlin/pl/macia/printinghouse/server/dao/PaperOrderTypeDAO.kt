package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.PaperOrderType

internal interface PaperOrderTypeDAO : JpaRepository<PaperOrderType, Int>, PaperOrderTypeDAOCustom