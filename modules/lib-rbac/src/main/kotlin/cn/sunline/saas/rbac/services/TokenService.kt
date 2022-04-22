package cn.sunline.saas.rbac.services

import io.jsonwebtoken.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

const val TOKEN_KEY_USERNAME = "username"
const val TOKEN_KEY_USERID = "user_id"
const val TOKEN_KEY_ROLES = "roles"

@Service
class TokenService {
    fun generateToken(userId: Long, user: UserDetails, key: String, expire: Date): String {
        val claims = Jwts.claims(mapOf(
                TOKEN_KEY_USERNAME to user.username,
                TOKEN_KEY_USERID to userId,
//                TOKEN_KEY_ROLES to user.authorities.map { it.authority }
        ))
//
//        val claims = Jwts.claims(mapOf(
//            TOKEN_KEY_USERNAME to user.username,
//            TOKEN_KEY_USERID to userId,
//            TOKEN_KEY_ROLES to user.authorities.map { it.authority }
//        ))


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date())
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, key.toByteArray())
                .compact()
    }

    fun validateToken(username: String, token: String, key: String): Map<String, Any>? {
        return try {
            val claims = Jwts.parser().setSigningKey(key.toByteArray()).parseClaimsJws(token).body.toMap()
            return claims.toMap()
        } catch (ex: ExpiredJwtException) {
            null
        } catch (ex: MalformedJwtException) {
            null
        } catch (ex: SignatureException) {
            null
        }
    }
}