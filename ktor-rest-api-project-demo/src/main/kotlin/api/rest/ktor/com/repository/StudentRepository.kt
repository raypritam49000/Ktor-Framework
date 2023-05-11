package api.rest.ktor.com.repository

import api.rest.ktor.com.dao.StudentDao
import api.rest.ktor.com.database.DatabaseFactory
import api.rest.ktor.com.dto.Student
import api.rest.ktor.com.entities.StudentTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class StudentRepository : StudentDao {

    override suspend fun createStudent(name: String, age: Int): Student? {
        var statement: InsertStatement<Number>? = null;
        DatabaseFactory.dbQuery {
            statement = StudentTable.insert { student ->
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }
        }
        return statement?.resultedValues?.get(0)?.let { rowToStudent(it) }
    }

    override suspend fun getStudents(): List<Student>? =
        DatabaseFactory.dbQuery {
            StudentTable.selectAll().mapNotNull {
                rowToStudent(it)
            }
        }


    override suspend fun getStudentById(studentId: Int): Student? =
        DatabaseFactory.dbQuery {
            StudentTable.select { StudentTable.studentId.eq(studentId) }
                .map {
                    rowToStudent(it)
                }.singleOrNull()
        }


    override suspend fun deleteStudentById(studentId: Int): Int? =
        DatabaseFactory.dbQuery {
            StudentTable.deleteWhere { StudentTable.studentId.eq(studentId) }
        }

    override suspend fun updateStudentById(studentId: Int, name: String, age: Int): Int? =
        DatabaseFactory.dbQuery {
            StudentTable.update({ StudentTable.studentId.eq(studentId) }) { student ->
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }
        }


    private fun rowToStudent(row: ResultRow): Student? {
        return Student(
            studentId = row[StudentTable.studentId],
            name = row[StudentTable.name],
            age = row[StudentTable.age]
        )
    }
}