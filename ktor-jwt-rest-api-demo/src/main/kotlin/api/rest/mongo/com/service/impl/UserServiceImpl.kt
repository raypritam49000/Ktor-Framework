package api.rest.mongo.com.service.impl

import api.rest.mongo.com.dbconfig.DatabaseFactory
import api.rest.mongo.com.dto.CreateUserParams
import api.rest.mongo.com.dto.User
import api.rest.mongo.com.entities.UserTable
import api.rest.mongo.com.security.PasswordEncoder
import api.rest.mongo.com.service.UserService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

class UserServiceImpl : UserService {

    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null
        val result = DatabaseFactory.dbQuery {
            statement = UserTable.insert {
                it[email] = params.email
                it[password] = PasswordEncoder.hash(params.password)
                it[full_name] = params.fullName
                it[avatar] = params.avatar
                it[role] = params.role
            }
            rowToUser(statement?.resultedValues?.get(0))
        }
        return result
    }

    override suspend fun findUserByEmail(email: String): User? =
        DatabaseFactory.dbQuery {
            UserTable.select { UserTable.email.eq(email) }
                .map {
                    rowToUser(it)
                }.singleOrNull()
        }

    override suspend fun isEmailExist(email: String): Boolean {
        return this.findUserByEmail(email) != null
    }

    override suspend fun findAll(): List<User>? {
        return DatabaseFactory.dbQuery {
            UserTable.selectAll().mapNotNull {
                rowToUser(it)
            }
        }
    }

    override suspend fun findById(id: Int): User? {
        return DatabaseFactory.dbQuery {
            UserTable.select { UserTable.userId.eq(id) }
                .map {
                    rowToUser(it)
                }.singleOrNull()
        }
    }

    override suspend fun deleteById(id: Int): Int? {
        return DatabaseFactory.dbQuery {
            UserTable.deleteWhere { UserTable.userId.eq(id) }
        }
    }


    override suspend fun updateUser(id: Int, user: User): Int? {
        val result = DatabaseFactory.dbQuery {
            UserTable.update({ UserTable.userId.eq(id) }) { userTable ->
                userTable[UserTable.full_name] = user.fullName
                userTable[UserTable.email] = user.email
                userTable[UserTable.avatar] = user.avatar
                userTable[UserTable.password] = PasswordEncoder.hash(user.password)
                userTable[UserTable.role] = user.role
            }
        }

        return result
    }


    private fun rowToUser(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            userId = row[UserTable.userId],
            fullName = row[UserTable.full_name],
            avatar = row[UserTable.avatar],
            email = row[UserTable.email],
            password = row[UserTable.password],
            createdAt = row[UserTable.createdAt].toString(),
            role = row[UserTable.role].toString()
        )
    }
}