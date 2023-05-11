package api.rest.ktor.com.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityUser : Table<Nothing>(tableName = "user") {
    val userId = int(name = "userId").primaryKey()
    val firstName = varchar(name = "firstName")
    val lastName = varchar(name = "lastName")
    val dob = varchar(name = "dob")
    val gender = varchar(name = "gender")
}

