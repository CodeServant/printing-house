package pl.macia.printinghouse.server.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtService {
    //todo secret should be fetched by env variable
    val secret =
        "975128164484985556498445174563245945218498421948945484975128164484985556498445174563245945218498421948945484"

    fun generateToken(userName: String): String {
        val claims: MutableMap<String, Any?> = HashMap()
        return createToken(claims, userName)
    }

    private fun createToken(claims: MutableMap<String, Any?>?, userName: String): String {
        return Jwts.builder()
            .claims(claims)
            .subject(userName)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(100000, ChronoUnit.SECONDS)))
            .signWith(getSignKey(), Jwts.SIG.HS256)
            .compact()
    }

    private fun getSignKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(token: String): String {
        return extractClaim(
            token,
            { it.getSubject() })
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(
            token,
            { it.getExpiration() })
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date.from(Instant.now()))
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.getUsername() && !isTokenExpired(token))
    }
}