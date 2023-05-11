package api.rest.cookie.com.security

import org.mindrot.jbcrypt.BCrypt

object PasswordEncoder {

    fun encoder(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun match(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}