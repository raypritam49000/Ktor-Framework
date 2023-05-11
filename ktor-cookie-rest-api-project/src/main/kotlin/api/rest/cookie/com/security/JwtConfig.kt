package api.rest.cookie.com.security

import api.rest.mongo.com.dto.User
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtConfig private constructor(secret: String) {

    private val algorithm = Algorithm.HMAC256(secret);

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun createAccessToken(user: User): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim("id",user.userId )
        .withClaim("fullName",user.fullName)
        .withClaim("avatar",user.avatar)
        .withClaim("email",user.email)
        .withClaim("createdAt",user.createdAt)
        .withClaim("role",user.role)
        .withExpiresAt(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
        .sign(algorithm)


    companion object {
        private const val ISSUER = "my-story-app"
        private const val AUDIENCE = "my-story-app"

        lateinit var instance: JwtConfig
            private set

        fun initialize(secret: String) {
            synchronized(this) {
                if(!this::instance.isInitialized){
                    instance = JwtConfig(secret)
                }
            }
        }
    }

}