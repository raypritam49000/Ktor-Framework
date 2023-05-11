package api.rest.ktor.com.routes

import api.rest.ktor.com.API_VERSION
import api.rest.ktor.com.dto.Student
import api.rest.ktor.com.repository.StudentRepository
import api.rest.ktor.com.utils.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val CREATE_STUDENT: String = "$API_VERSION/createStudent"
const val GET_STUDENTS: String = "$API_VERSION/getStudents"
const val GET_STUDENT: String = "$API_VERSION/getStudent/{studentId}"
const val DELETE_STUDENT: String = "$API_VERSION/deleteStudent/{studentId}"
const val UPDATE_STUDENT: String = "$API_VERSION/updateStudent/{studentId}"

fun Application.studentRoutes(studentRepository: StudentRepository) {
    routing {

        post(CREATE_STUDENT) {
            val requestBody = call.receive<Student>()
            try {
                val student = studentRepository.createStudent(requestBody.name, requestBody.age)
                student?.studentId?.let {
                    return@post call.respond(status = HttpStatusCode.OK, GenericResponse(isSuccess = true, data = student))
                }
            } catch (e: Throwable) {
                return@post call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = e.message ?: "Unknown error"
                )
            }
        }

        get(GET_STUDENTS) {
            try {
                val students: List<Student>? = studentRepository.getStudents()
                return@get call.respond(status = HttpStatusCode.OK, GenericResponse(isSuccess = true, data = students))
            } catch (e: Throwable) {
                return@get call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = e.message ?: "Unknown error"
                )
            }

        }

        get(GET_STUDENT) {
            try {
                val studentId = call.parameters["studentId"]?.toInt();
                val student: Student? = studentId?.let { it1 -> studentRepository.getStudentById(it1) };
                return@get call.respond(status = HttpStatusCode.OK, GenericResponse(isSuccess = true, data = student))
            } catch (e: Throwable) {
                return@get call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = e.message ?: "Unknown error"
                )
            }

        }

        delete(DELETE_STUDENT) {
            try {
                val studentId = call.parameters["studentId"]?.toInt();
                val rowsEffects: Int? = studentId?.let { it1 -> studentRepository.deleteStudentById(it1) };

                if (rowsEffects != null) {
                    if (rowsEffects <= 0) {
                        return@delete call.respond(
                            status = HttpStatusCode.OK,
                            GenericResponse(isSuccess = true, data = "Student does not found!! ")
                        )
                    } else {
                        return@delete call.respond(
                            status = HttpStatusCode.OK,
                            GenericResponse(isSuccess = true, data = "Student has been deleted")
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

        put(UPDATE_STUDENT) {
            try {
                val studentId = call.parameters["studentId"]?.toInt();
                val requestBody = call.receive<Student>();
                val rowsEffects: Int? =
                    studentId?.let { id -> studentRepository.updateStudentById(id, requestBody.name, requestBody.age) };

                if (rowsEffects != null) {
                    if (rowsEffects <= 0) {
                        return@put call.respond(
                            status = HttpStatusCode.OK,
                            GenericResponse(isSuccess = true, data = "Student does not found!! ")
                        )
                    } else {
                        return@put call.respond(
                            status = HttpStatusCode.OK,
                            GenericResponse(isSuccess = true, data = "Student has been updated")
                        )
                    }
                }

            } catch (e: Throwable) {
                return@put call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = e.message ?: "Unknown error"
                )
            }
        }


    }
}
