package com.inventorypos.data.remote.api

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val code: Int = 0) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}
