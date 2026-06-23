package com.inventorypos.domain.usecase.auth

import com.inventorypos.data.remote.api.ApiResponse
import com.inventorypos.data.remote.model.AuthDto
import com.inventorypos.data.remote.repository.RemoteRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke(request: AuthDto.RegisterRequest): ApiResponse<AuthDto.RegisterResponse> {
        // TODO: Implement when API ready
        return ApiResponse.Error("Not implemented")
    }
}
