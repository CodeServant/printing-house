package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.IndividualClient

internal interface IndividualClientDAO : JpaRepository<IndividualClient, Int>, IndividualClientDAOCustom