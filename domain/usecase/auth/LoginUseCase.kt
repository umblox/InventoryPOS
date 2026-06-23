package com.inventorypos.domain.usecase.auth

import com.inventorypos.data.remote.api.ApiResponse
import com.inventorypos.data.remote.model.AuthDto
import com.inventorypos.data.remote.repository.RemoteRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke(username: String, password: String): ApiResponse<AuthDto.LoginResponse> {
        return remoteRepository.login(username, password)
    }
}
