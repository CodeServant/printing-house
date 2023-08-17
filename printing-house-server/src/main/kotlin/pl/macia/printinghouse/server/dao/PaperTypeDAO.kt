package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.PaperType

interface PaperTypeDAO : JpaRepository<PaperType, Int>, PaperTypeDAOCustom