package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Client

internal interface ClientDAO : JpaRepository<Client, Int>, ClientDAOCustom