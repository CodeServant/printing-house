package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.CalculationCard

interface CalculationCardDAO : JpaRepository<CalculationCard, Int>, CalculationCardDAOCustom