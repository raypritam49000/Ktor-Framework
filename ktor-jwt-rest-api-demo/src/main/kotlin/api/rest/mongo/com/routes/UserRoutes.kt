package api.rest.mongo.com.routes

import api.rest.mongo.com.dto.User
import api.rest.mongo.com.service.UserService
import api.rest.mongo.com.utils.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutes(userService: UserService) {

    routing {

        authenticate {

            route("/users") {

                get("/hello") {
                    return@get call.respond(HttpStatusCode.OK, "Welcome to KTOR Framework...")
                }

                get("/getAllUsers") {
                    try {
                        val users: List<User>? = userService.findAll();
                        return@get call.respond(HttpStatusCode.OK, mapOf("users" to users))
                    } catch (e: Throwable) {
                        return@get call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = e.message ?: "Unknown error"
                        )
                    }
                }

                get("/getUser/{userId}") {
                    try {
                        val userId = call.parameters["userId"]?.toInt();
                        val user: User? = userId?.let { it1 -> userService.findById(it1) };
                        return@get call.respond(
                            status = HttpStatusCode.OK,
                            GenericResponse(isSuccess = true, data = user)
                        )
                    } catch (e: Throwable) {
                        return@get call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = e.message ?: "Unknown error"
                        )
                    }
                }

                delete("/deleteUser") {
                    try {
                        val userId = call.parameters["userId"]?.toInt();
                        val rowsEffects: Int? = userId?.let { it1 -> userService.deleteById(it1) };

                        if (rowsEffects != null) {
                            if (rowsEffects <= 0) {
                                return@delete call.respond(
                                    status = HttpStatusCode.OK,
                                    GenericResponse(isSuccess = true, data = "User does not found!! ")
                                )
                            } else {
                                return@delete call.respond(
                                    status = HttpStatusCode.OK,
                                    GenericResponse(isSuccess = true, data = "User has been deleted")
                                )
                            }
                        }
                    } catch (e: Throwable) {
                        return@delete call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = e.message ?: "Unknown error"
                        )
                    }
                }

                put("/updateUser/{userId}") {
                    val userId = call.parameters["userId"]?.toInt();
                    val requestBody: User = call.receive<User>();
                    val rowsEffects: Int? =
                        userId?.let { id -> userService.updateUser(id, requestBody) };

                    if (rowsEffects != null) {
                        if (rowsEffects <= 0) {
                            return@put call.respond(
                                status = HttpStatusCode.OK,
                                GenericResponse(isSuccess = true, data = "User does not found!! ")
                            )
                        } else {
                            return@put call.respond(
                                status = HttpStatusCode.OK,
                                GenericResponse(isSuccess = true, data = "User has been updated")
                            )
                        }
                    }
                }

            }
        }

    }

}