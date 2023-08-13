package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Enobling

interface EnoblingDAO : JpaRepository<Enobling, Int>, EnoblingDAOCustom