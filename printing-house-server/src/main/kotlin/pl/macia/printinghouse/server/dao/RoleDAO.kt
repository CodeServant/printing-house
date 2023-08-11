package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Role

interface RoleDAO : JpaRepository<Role, Int>, RoleDAOCustom