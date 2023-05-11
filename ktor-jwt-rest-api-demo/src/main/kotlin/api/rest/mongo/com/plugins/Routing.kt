package api.rest.mongo.com.plugins

import api.rest.mongo.com.dbconfig.DatabaseFactory
import api.rest.mongo.com.routes.authRoutes
import api.rest.mongo.com.routes.userRoutes
import api.rest.mongo.com.service.impl.UserServiceImpl
import io.ktor.server.application.*

fun Application.configureRouting() {
    DatabaseFactory.init()
    configureSerialization()
    configureSecurity()
    val userService = UserServiceImpl();
    authRoutes(userService)
    userRoutes(userService);
}
