package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Role
import pl.macia.printinghouse.server.bmodel.RoleImpl
import pl.macia.printinghouse.server.dto.Role as PRole
import pl.macia.printinghouse.server.dao.RoleDAO

@Repository
internal class RoleRepoImpl : RoleIntRepo {
    @Autowired
    lateinit var dao: RoleDAO
    private fun PRole.toBiz() = RoleImpl(this)
    override fun save(obj: Role): Role {
        obj as RoleImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Role? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findAllById(roleIds: Iterable<Int>): Set<Role> {
        return dao.findAllById(roleIds).map { it.toBiz() }.toSet()
    }
}