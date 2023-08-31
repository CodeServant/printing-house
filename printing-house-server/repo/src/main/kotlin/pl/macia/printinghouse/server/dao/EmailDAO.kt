package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Email

internal interface EmailDAO : JpaRepository<Email, Int>, EmailDAOCustom