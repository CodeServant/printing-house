package pl.macia.printinghouse.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun encoder(): PasswordEncoder {
        val encoders: MutableMap<String, PasswordEncoder> = HashMap()
        encoders["bcrypt"] = BCryptPasswordEncoder()
        return DelegatingPasswordEncoder("bcrypt", encoders)
    }
}