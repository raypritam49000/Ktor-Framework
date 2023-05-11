package api.rest.cookie.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val email: String,
    val password: String
)