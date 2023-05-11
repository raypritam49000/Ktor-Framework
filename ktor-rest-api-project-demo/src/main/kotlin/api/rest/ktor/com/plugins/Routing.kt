package api.rest.ktor.com.plugins

import api.rest.ktor.com.database.DatabaseFactory
import api.rest.ktor.com.repository.StudentRepository
import api.rest.ktor.com.routes.studentRoutes
import api.rest.ktor.com.serializations.configureSerialization
import io.ktor.server.application.*

fun Application.configureRouting() {
    DatabaseFactory.init()
    configureSerialization()
    val sudentRepository = StudentRepository()
    studentRoutes(sudentRepository)
}
