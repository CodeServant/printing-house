package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Image

internal interface ImageDAO : JpaRepository<Image, Long>, ImageDAOCustom