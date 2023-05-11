package api.rest.mongo.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int? = null,
    val fullName: String,
    val avatar: String,
    val email: String,
    var password: String,
    val role:String,
    val createdAt: String? =null
)