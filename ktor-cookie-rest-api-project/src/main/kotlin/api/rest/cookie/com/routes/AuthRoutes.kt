package api.rest.cookie.com.routes

import api.rest.cookie.com.dto.CreateUserParams
import api.rest.cookie.com.dto.JwtToken
import api.rest.cookie.com.security.JwtConfig
import api.rest.cookie.com.security.PasswordEncoder
import api.rest.mongo.com.dto.User
import api.rest.cookie.com.service.UserService
import api.rest.mongo.com.utils.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.java.KoinJavaComponent.inject
import java.util.*


fun Application.authRoutes() {

    val userService: UserService by inject(UserService::class.java)


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
                            val isMatchPassword: Boolean = PasswordEncoder.match(params.password, user.password)
                            if (!isMatchPassword) {
                                return@post call.respond(HttpStatusCode.Forbidden, "Password does not match")
                            } else {
                                val token = JwtConfig.instance.createAccessToken(user);

                                val date = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)
                                val intDate =
                                    date.time.toInt() / 1000 // divide by 1000 to convert from milliseconds to seconds

                                call.sessions.set(JwtToken(token))

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

            authenticate {
                get("/logout"){
                    //destroy cookie
                    call.sessions.clear("JWT")
                }
            }

        }

    }

}