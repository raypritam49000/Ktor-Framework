package jaocom

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import jaocom.plugins.*

fun main() {
    embeddedServer(Netty, port = 9988, host = "localhost") {
        configureSecurity()
        configureTemplating()
        configureStatusPages()
        configureRouting()
        configureMonitoring()
    }.start(wait = true)
}
