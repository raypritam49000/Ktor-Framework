package api.rest.ktor.com

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import api.rest.ktor.com.plugins.*
import api.rest.ktor.com.routes.configureSerialization

fun main() {
    embeddedServer(Netty, port = 9999, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

