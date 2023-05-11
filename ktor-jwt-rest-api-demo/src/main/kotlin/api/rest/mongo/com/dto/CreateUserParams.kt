package api.rest.mongo.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserParams(
    val fullName: String = "",
    val avatar: String = "",
    val email: String = "",
    var password: String = "",
    val role: String = ""
)
