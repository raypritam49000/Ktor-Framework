package api.rest.ktor.com.plugins

import api.rest.ktor.com.routes.routeUser
import io.ktor.server.application.*

fun Application.configureRouting() {
    routeUser();
}
