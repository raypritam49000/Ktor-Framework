package api.rest.mongo.com.plugins

import api.rest.mongo.com.database.DatabaseFactory
import api.rest.mongo.com.routes.userRoutes
import api.rest.mongo.com.serializations.configureSerialization
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureSerialization()
    val databaseFactory = DatabaseFactory();
    userRoutes(databaseFactory);
}
