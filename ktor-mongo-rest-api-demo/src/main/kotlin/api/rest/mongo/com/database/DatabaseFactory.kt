package api.rest.mongo.com.database

import api.rest.mongo.com.enities.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class DatabaseFactory {
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("users")
    private val userCollection: CoroutineCollection<User> = database.getCollection()

    suspend fun addUser(user: User): User {
        userCollection.insertOne(user)
        return user;
    }

    suspend fun getAllUser(): List<User> = userCollection.find().toList();

    suspend fun getUserById(userId: String): User? = userCollection.findOneById(userId)

    suspend fun deleteUserById(userId: String): Boolean {
        val result = userCollection.deleteOneById(userId)
        return result.wasAcknowledged() && result.deletedCount > 0
    }

    suspend fun updateUserById(userId: String, updatedUser: User): Boolean = userCollection.updateOneById(userId, updatedUser).wasAcknowledged()
}