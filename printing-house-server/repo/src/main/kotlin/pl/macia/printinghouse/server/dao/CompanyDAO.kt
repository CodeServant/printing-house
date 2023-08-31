package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Company

internal interface CompanyDAO : JpaRepository<Company, Int>, CompanyDAOCustom