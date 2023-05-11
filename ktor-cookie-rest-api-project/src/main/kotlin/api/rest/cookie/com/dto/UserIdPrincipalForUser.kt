package api.rest.cookie.com.dto

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserIdPrincipalForUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val avatar: String,
    val createdAt: String,
    val role:String
) : Principal
