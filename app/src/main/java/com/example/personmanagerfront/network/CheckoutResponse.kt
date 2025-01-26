package com.example.personmanagerfront.network

data class CheckoutRequest(
    val name: String,
    val address: String,
    val phone: String
)

data class CheckoutResponse(
    val message: String
)