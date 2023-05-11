package api.rest.mongo.com.plugins

import api.rest.mongo.com.dto.UserIdPrincipalForUser
import api.rest.mongo.com.security.JwtConfig
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*

fun Application.configureSecurity() {

    JwtConfig.initialize("my-store-app")

    authentication {
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val id: Int = it.payload.getClaim("id").asInt()
                val fullName: String = it.payload.getClaim("fullName").asString()
                val email: String = it.payload.getClaim("email").asString()
                val avatar: String = it.payload.getClaim("avatar").asString()
                val createdAt: String = it.payload.getClaim("createdAt").asString()
                val role : String = it.payload.getClaim("role").asString()
                UserIdPrincipalForUser(id, fullName, email, avatar, createdAt,role)
            }
        }
    }

}

