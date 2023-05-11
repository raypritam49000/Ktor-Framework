package api.rest.ktor.com.database

import api.rest.ktor.com.entities.StudentTable
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private val conf: Config = ConfigFactory.load()

    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(StudentTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val hikariConfig = HikariConfig();
        hikariConfig.driverClassName = conf.getString("database.driver")
        hikariConfig.jdbcUrl = conf.getString("database.url")
        hikariConfig.username = conf.getString("database.username")
        hikariConfig.password = conf.getString("database.password")
        hikariConfig.maximumPoolSize = conf.getInt("database.maximumPoolSize")
        hikariConfig.isAutoCommit = conf.getBoolean("database.isAutoCommit")
        hikariConfig.transactionIsolation = conf.getString("database.transactionIsolation")
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction {
                block()
            }
        }

}
