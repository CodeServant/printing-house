package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.URL

interface UrlDAO : JpaRepository<URL, Long>, UrlDAOCustom