package api.rest.mongo.com.enities
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    @BsonId
    val userId:String? = ObjectId().toString(),
    val username:String?,
    val email:String?,
    val password:String?,
)
