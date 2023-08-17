package pl.macia.printinghouse.server.dto

import org.springframework.data.jpa.repository.JpaRepository

interface ImpositionTypeDAO:JpaRepository<ImpositionType,Int>, ImpositionTypeDAOCustom