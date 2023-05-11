package api.rest.mongo.com.service

import api.rest.mongo.com.dto.CreateUserParams
import api.rest.mongo.com.dto.User

interface UserService {
    suspend fun registerUser(params: CreateUserParams): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun isEmailExist(email: String): Boolean
    suspend fun findAll(): List<User>?
    suspend fun findById(id: Int): User?
    suspend fun deleteById(id: Int): Int?
    suspend fun updateUser(id: Int, user: User): Int?
}