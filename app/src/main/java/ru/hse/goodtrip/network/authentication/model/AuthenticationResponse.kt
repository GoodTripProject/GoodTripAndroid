package ru.hse.goodtrip.network.authentication.model

data class AuthenticationResponse(
        val handle: String,
        val name: String,
        val surname: String,
        val token: String,
        val url: java.net.URL?
)
