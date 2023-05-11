package api.rest.ktor.com.utils

data class GenericResponse<out T>(val isSuccess:Boolean, val data: T)
