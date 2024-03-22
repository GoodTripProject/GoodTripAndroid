package ru.hse.goodtrip.network.authentication.model

import lombok.Builder

@Builder
data class RegisterRequest(

        val username: String,
        val handle: String,
        val password: String,
        val name: String,
        val surname: String
)
