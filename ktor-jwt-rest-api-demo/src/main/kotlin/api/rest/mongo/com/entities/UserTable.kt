package api.rest.mongo.com.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable : Table() {
    val userId: Column<Int> = integer("userId").autoIncrement()
    val full_name : Column<String> = varchar("full_name", 256)
    val avatar : Column<String> = text("avatar")
    val email : Column<String>  = varchar("email", 256)
    val password :  Column<String>  = text("password")
    val role : Column<String>  = varchar("role", 256)
    val createdAt : Column<LocalDateTime> = datetime("created_at").clientDefault { LocalDateTime.now() }

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}