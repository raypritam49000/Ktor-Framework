package api.rest.ktor.com.routes

import api.rest.ktor.com.database.DbConnection
import api.rest.ktor.com.dto.User
import api.rest.ktor.com.entities.EntityUser
import api.rest.ktor.com.utils.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeUser() {

    val db: Database = DbConnection.getDatabaseInstance();

    routing {

        get("/") {
            call.respondText("Welcome to Ktor MySQL");
        }

        post("/addUser") {
            val user: User = call.receive<User>();

            val noOfRowsEffected = db.insert(EntityUser) {
                set(it.firstName, user.firstName)
                set(it.lastName, user.lastName)
                set(it.dob, user.dob)
                set(it.gender, user.gender)
            }

            if (noOfRowsEffected > 0) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(isSuccess = true, data = "$noOfRowsEffected rows are effected")
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(isSuccess = false, data = "Error to add user")
                )
            }
        }

        get("/getAllUsers") {
            val userList: List<User> = db.from(EntityUser).select().map { row ->
                User(
                    row[EntityUser.userId],
                    row[EntityUser.firstName],
                    row[EntityUser.lastName],
                    row[EntityUser.dob],
                    row[EntityUser.gender]
                )
            }
            call.respond(
                HttpStatusCode.OK,
                GenericResponse(isSuccess = true, data = userList)
            )
        }


        get("/getUser/{id}") {
            val id: Int? = call.parameters["id"]?.toInt()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val query: Query = db.from(EntityUser).select().where { EntityUser.userId eq id };
            var user: User? = null;

            query.forEach { row ->
                user = User(
                    row[EntityUser.userId],
                    row[EntityUser.firstName],
                    row[EntityUser.lastName],
                    row[EntityUser.dob],
                    row[EntityUser.gender]
                )
            };

            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    GenericResponse(isSuccess = true, data = "User does not found")
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(isSuccess = true, data = user)
                )
            }
        }

        put("/updateUser/{id}") {
            val id: Int? = call.parameters["id"]?.toInt()
            val user: User = call.receive<User>();

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            db.update(EntityUser) {
                set(it.firstName, user.firstName)
                set(it.lastName, user.lastName)
                set(it.dob, user.dob)
                set(it.gender, user.gender)
                where { EntityUser.userId eq id }
            }

            call.respond(
                HttpStatusCode.OK,
                GenericResponse(isSuccess = true, data = "User has been updated")
            )
        }

        delete("/deleteUser/{id}") {
            val id: Int? = call.parameters["id"]?.toInt()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val deletedCount = db.delete(EntityUser){
                it.userId eq  id
            }

            if (deletedCount == 0) {
                call.respond(
                    HttpStatusCode.NotFound,
                    GenericResponse(isSuccess = false, data = "User not found")
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(isSuccess = true, data = "User has been deleted")
                )
            }
        }


    }
}
