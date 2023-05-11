package api.rest.mongo.com.security

import org.mindrot.jbcrypt.BCrypt

object PasswordEncoder {

    fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verify(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}