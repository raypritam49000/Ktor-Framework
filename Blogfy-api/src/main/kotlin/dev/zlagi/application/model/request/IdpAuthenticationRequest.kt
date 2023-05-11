package dev.zlagi.application.model.request

import kotlinx.serialization.Serializable

@Serializable
data class IdpAuthenticationRequest(
    val username: String
)