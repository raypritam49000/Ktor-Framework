package api.rest.cookie.com

import io.ktor.server.application.*
import api.rest.cookie.com.plugins.*
import api.rest.cookie.com.dbconfig.DatabaseFactory
import api.rest.cookie.com.di.myModule
import org.koin.core.context.GlobalContext.startKoin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val modules = listOf(myModule)
    val koin = startKoin {
        modules(modules)
    }.koin

    DatabaseFactory.init()
    configureRouting()
}
