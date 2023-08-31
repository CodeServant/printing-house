package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Person

internal interface PersonDAO : JpaRepository<Person, Int>, PersonDAOCustom