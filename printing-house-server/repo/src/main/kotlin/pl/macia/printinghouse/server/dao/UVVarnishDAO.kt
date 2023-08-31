package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.UVVarnish

internal interface UVVarnishDAO : JpaRepository<UVVarnish, Int>, UVVarnishDAOCustom