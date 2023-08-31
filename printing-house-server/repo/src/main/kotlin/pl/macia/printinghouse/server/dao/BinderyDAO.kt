package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Bindery

internal interface BinderyDAO : JpaRepository<Bindery, Int>, BinderyCustomDAO