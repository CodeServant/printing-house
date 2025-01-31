package pl.macia.printinghouse.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    private lateinit var authFilter: JwtAuthFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement({ s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            })
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun encoder(): PasswordEncoder {
        val encoders: MutableMap<String, PasswordEncoder> = HashMap()
        encoders["bcrypt"] = BCryptPasswordEncoder()
        return DelegatingPasswordEncoder("bcrypt", encoders)
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(
        configuration: AuthenticationConfiguration
    ): AuthenticationManager {
        return configuration.authenticationManager
    }
}