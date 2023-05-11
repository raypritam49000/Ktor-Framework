package api.rest.cookie.com.plugins

import api.rest.cookie.com.routes.authRoutes
import api.rest.cookie.com.routes.userRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureSerialization()
    configureSecurity()
    authRoutes()
    userRoutes()
}
