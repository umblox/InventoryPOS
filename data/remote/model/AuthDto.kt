package com.inventorypos.data.remote.model

import com.google.gson.annotations.SerializedName

object AuthDto {
    data class LoginRequest(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
    )

    data class LoginResponse(
        @SerializedName("token") val token: String,
        @SerializedName("user") val user: UserDto
    )

    data class RegisterRequest(
        @SerializedName("full_name") val fullName: String,
        @SerializedName("username") val username: String,
        @SerializedName("email") val email: String?,
        @SerializedName("password") val password: String
    )

    data class RegisterResponse(
        @SerializedName("message") val message: String,
        @SerializedName("user") val user: UserDto
    )

    data class UserDto(
        @SerializedName("id") val id: Long,
        @SerializedName("username") val username: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("email") val email: String?,
        @SerializedName("role") val role: String
    )
}
