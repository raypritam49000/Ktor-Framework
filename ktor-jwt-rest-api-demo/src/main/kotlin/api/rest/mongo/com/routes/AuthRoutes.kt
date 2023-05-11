package api.rest.mongo.com.routes

import api.rest.mongo.com.dto.CreateUserParams
import api.rest.mongo.com.dto.User
import api.rest.mongo.com.security.JwtConfig
import api.rest.mongo.com.security.PasswordEncoder
import api.rest.mongo.com.service.UserService
import api.rest.mongo.com.utils.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.authRoutes(userService: UserService) {

    routing {

        route("/auth") {

            post("/register") {
                try {
                    val params: CreateUserParams = call.receive<CreateUserParams>();

                    if (userService.isEmailExist(params.email)) {
                        return@post call.respond(HttpStatusCode.Conflict, "Email already register")
                    } else {
                        val user: User? = userService.registerUser(params);
                        return@post call.respond(HttpStatusCode.Created, GenericResponse(isSuccess = true, data = user))
                    }
                } catch (e: Throwable) {
                    return@post call.respond(HttpStatusCode.InternalServerError, e.message.orEmpty());
                }
            }

            post("/login") {
                try {
                    val params: CreateUserParams = call.receive<CreateUserParams>();

                    if (!userService.isEmailExist(params.email)) {
                        return@post call.respond(HttpStatusCode.NotFound, "Email does not register")
                    } else {
                        val user: User? = userService.findUserByEmail(params.email);
                        if (user != null) {
                            val isMatchPassword: Boolean = PasswordEncoder.verify(params.password, user.password)
                            if (!isMatchPassword) {
                                return@post call.respond(HttpStatusCode.Forbidden, "Password does not match")
                            } else {
                                val token = JwtConfig.instance.createAccessToken(user);
                                return@post call.respond(HttpStatusCode.OK, mapOf("type" to "Bearer", "token" to token))
                            }
                        } else {
                            return@post call.respond(HttpStatusCode.NotFound, "Email does not register")
                        }
                    }

                } catch (e: Throwable) {
                    return@post call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = e.message ?: "Unknown error"
                    )
                }
            }

        }

    }
}