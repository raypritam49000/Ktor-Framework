package api.rest.mongo.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val email: String,
    val password: String
)