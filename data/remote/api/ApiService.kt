package com.inventorypos.data.remote.api

import com.inventorypos.data.remote.model.AuthDto
import com.inventorypos.data.remote.model.ProductDto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthDto.LoginRequest): Response<AuthDto.LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: AuthDto.RegisterRequest): Response<AuthDto.RegisterResponse>

    @GET("products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Long): Response<ProductDto>

    @POST("products")
    suspend fun createProduct(@Body product: ProductDto): Response<ProductDto>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: ProductDto): Response<ProductDto>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): Response<List<ProductDto>>
}
