package api.rest.mongo.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<out T>(
    val isSuccess: Boolean,
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: T
)
