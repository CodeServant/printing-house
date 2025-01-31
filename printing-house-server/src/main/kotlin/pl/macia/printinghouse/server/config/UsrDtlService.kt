package pl.macia.printinghouse.server.config

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pl.macia.printinghouse.server.repository.EmployeeRepo

@Service("userDetailsService")
@Transactional
class UsrDtlService : UserDetailsService {
    @Autowired
    private lateinit var employeeRepo: EmployeeRepo
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null)
            throw UsernameNotFoundException("null value passed")
        val worker = employeeRepo.findByEmail(username)
        if (worker == null)
            throw UsernameNotFoundException("user not found")
        return User(
            worker.email.email,
            worker.password,
            worker.activeAccount,
            true,
            true,
            true,
            worker.roles
                .map { SimpleGrantedAuthority(it.name) }
        )
    }
}