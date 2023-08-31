package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Role

internal interface RoleDAO : JpaRepository<Role, Int>, RoleDAOCustom