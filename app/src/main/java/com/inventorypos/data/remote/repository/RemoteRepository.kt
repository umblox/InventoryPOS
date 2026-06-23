package com.inventorypos.data.remote.repository

import com.inventorypos.data.remote.api.ApiResponse
import com.inventorypos.data.remote.api.ApiService
import com.inventorypos.data.remote.model.AuthDto
import com.inventorypos.data.remote.model.ProductDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): ApiResponse<AuthDto.LoginResponse> {
        return try {
            val response = apiService.login(AuthDto.LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Network error")
        }
    }

    suspend fun getProducts(): ApiResponse<List<ProductDto>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Error(response.message())
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Network error")
        }
    }
}
