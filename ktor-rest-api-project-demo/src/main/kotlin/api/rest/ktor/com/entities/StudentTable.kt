package api.rest.ktor.com.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object StudentTable : Table() {
    val studentId: Column<Int> = integer("studentId").autoIncrement()
    val name: Column<String> = varchar("name", 512)
    val age: Column<Int> = integer("age")

    override val primaryKey: PrimaryKey = PrimaryKey(studentId)
}