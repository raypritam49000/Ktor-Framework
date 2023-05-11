package api.rest.ktor.com.dao

import api.rest.ktor.com.dto.Student

interface StudentDao {
    suspend fun createStudent(name: String, age: Int): Student?
    suspend fun getStudents(): List<Student>?
    suspend fun getStudentById(studentId: Int): Student?
    suspend fun deleteStudentById(studentId: Int): Int?
    suspend fun updateStudentById(studentId: Int, name: String, age: Int): Int?
}