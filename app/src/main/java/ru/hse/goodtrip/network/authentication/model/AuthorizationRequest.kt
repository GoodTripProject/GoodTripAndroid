package ru.hse.goodtrip.network.authentication.model

import lombok.Builder

@Builder
data class AuthorizationRequest(
        val username: String,
        val password: String

)