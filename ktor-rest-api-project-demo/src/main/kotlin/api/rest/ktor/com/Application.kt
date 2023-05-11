package api.rest.ktor.com

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import api.rest.ktor.com.plugins.*
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

fun main() {
    val conf: Config = ConfigFactory.load()
    val port : Int = conf.getInt("ktor.deployment.port")
    val host : String = conf.getString("ktor.deployment.host")
    embeddedServer(Netty, port = port, host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}

const val API_VERSION = "/v1/"
