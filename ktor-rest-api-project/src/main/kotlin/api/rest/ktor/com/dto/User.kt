package api.rest.ktor.com.dto

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("userId")
    val userId: Int?,

    @field:SerializedName("firstName")
    val firstName: String? = null,

    @field:SerializedName("lastName")
    val lastName: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null
)
