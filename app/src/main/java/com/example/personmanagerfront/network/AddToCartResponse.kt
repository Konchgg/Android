package com.example.personmanagerfront.network

data class AddToCartResponse(
    val id: Int,
    val user: Int,
    val product: Knife, // Можно переиспользовать класс Knife
    val quantity: Int
)