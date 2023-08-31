package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.URL

internal interface UrlDAO : JpaRepository<URL, Long>, UrlDAOCustom