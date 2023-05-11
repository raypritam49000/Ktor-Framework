package api.rest.mongo.com.routes

import api.rest.mongo.com.database.DatabaseFactory
import api.rest.mongo.com.dto.ApiResponse
import api.rest.mongo.com.dto.UserDto
import api.rest.mongo.com.enities.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutes(db: DatabaseFactory) {

    routing {

        route("/user") {

            post("/") {
                val requestBody = call.receive<UserDto>();
                val user = db.addUser(User(null, requestBody.username, requestBody.email, requestBody.password));
                return@post call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        true,
                        201,
                        "Created",
                        "User has been created",
                        UserDto(user.userId, user.username, user.password, user.email)
                    )
                )
            }

            get("/") {

                val users: List<UserDto>? = db.getAllUser().map { user ->
                    UserDto(user.userId, user.username, user.password, user.email)
                }
                return@get call.respond(HttpStatusCode.OK, ApiResponse(true, 200, "OK", "Users Details Lists", users))
            }


            get("/{userId}") {
                val userId: String? = call.parameters["userId"].toString()
                val user: User? = userId?.let { it1 -> db.getUserById(it1) }
                if (user != null) {
                    return@get call.respond(HttpStatusCode.OK, ApiResponse(true, 200, "OK", "User Details", user))
                } else {
                    return@get call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse(false, 404, "Not Found", "User not found", "")
                    )
                }
            }

            delete("/{userId}") {
                val userId: String? = call.parameters["userId"].toString()
                val isDeleted: Boolean = userId?.let { db.deleteUserById(it) } ?: false
                if (isDeleted) {
                    return@delete call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(true, 200, "OK", "User has been deleted", "")
                    )
                } else {
                    return@delete call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse(false, 404, "Not Found", "User not found", "")
                    )
                }
            }

            put("/{userId}") {
                val userId: String? = call.parameters["userId"].toString()
                val userDto = call.receive<UserDto>()
                val user = userId?.let { it1 -> db.getUserById(it1) }

                if (user != null) {
                    // map UserDto to User entity
                    val updatedUser = user.copy(
                        username = userDto.username ?: user.username,
                        email = userDto.email ?: user.email,
                        password = userDto.password ?: user.password
                    )

                    // update user in the database
                    db.updateUserById(userId, updatedUser)

                    call.respond(HttpStatusCode.OK, ApiResponse(true, 200, "OK", "User has been updated", ""))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse(false, 404, "Not Found", "User not found", ""))
                }
            }

        }

    }
}