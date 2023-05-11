package api.rest.ktor.com.database

import org.ktorm.database.Database


object DbConnection {
    private val db: Database? = null
    fun getDatabaseInstance(): Database {
        return db ?: Database.connect(
            url = "jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "0003pray",
        )
    }
}