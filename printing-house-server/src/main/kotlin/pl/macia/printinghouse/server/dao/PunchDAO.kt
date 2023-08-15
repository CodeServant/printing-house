package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Punch

interface PunchDAO : JpaRepository<Punch, Int>, PunchDAOCustom