package api.rest.mongo.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: String? = null,
    val username: String? = null,
    val password: String? = null,
    val email: String? = null
)