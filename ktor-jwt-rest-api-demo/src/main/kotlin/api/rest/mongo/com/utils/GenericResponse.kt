package api.rest.mongo.com.utils

import kotlinx.serialization.Serializable

@Serializable
data class GenericResponse<out T>(val isSuccess:Boolean, val data: T)
