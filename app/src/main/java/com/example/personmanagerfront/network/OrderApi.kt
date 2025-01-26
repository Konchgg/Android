// D:\android_prj\PersonManagerFront\app\src\main\java\com\example\personmanagerfront\network\OrderApi.kt
package com.example.personmanagerfront.network

// Модель одного заказа
data class OrderApi(
    val id: Int,
    val user: String,
    val name: String,
    val address: String,
    val phone_number: String,
    val cart_items: List<CartItemApi>, // Если нужно отобразить товары; иначе можно List<Any>
    val status: String,
    val total_price: String,
    val created_at: String
)