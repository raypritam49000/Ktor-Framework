package api.rest.cookie.com.plugins

import api.rest.cookie.com.dto.JwtToken
import api.rest.cookie.com.dto.UserIdPrincipalForUser
import api.rest.cookie.com.security.JwtConfig
import api.rest.mongo.com.utils.GenericResponse
import io.github.omkartenkale.ktor_role_based_auth.UnauthorizedAccessException
import io.github.omkartenkale.ktor_role_based_auth.roleBased
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {

    JwtConfig.initialize("my-store-app")

    install(Sessions) {
        cookie<JwtToken>("JWT") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is UnauthorizedAccessException) {
                call.respond(HttpStatusCode.Unauthorized,GenericResponse(isSuccess = false,data="Access to Denied"))
            }
        }
    }

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

            //use cookie as jwt token
            authHeader {
                val oldHeader = it.request.parseAuthorizationHeader()
                val jwt = it.sessions.get<JwtToken>()
                jwt?.token?.let { token ->
                    HttpAuthHeader.Single(oldHeader?.authScheme ?: "Bearer", token)
                } ?: oldHeader
            }

            // role base authentication
            roleBased {
                extractRoles { principal ->
                    //Extract roles from JWT payload
                    // (principal as JWTPrincipal).payload.claims?.get("roles")?.asList(String::class.java)?.toSet() ?: emptySet()
                    setOf((principal as UserIdPrincipalForUser).role)
                }
                throwErrorOnUnauthorizedResponse = true
            }
        }
    }

}
