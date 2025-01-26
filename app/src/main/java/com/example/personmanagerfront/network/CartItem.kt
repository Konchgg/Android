package com.example.personmanagerfront.network

data class CartItemApi(
    val id: Int,           // <-- ВАЖНО: это то самое "id", по которому будем удалять
    val product_id: Int,
    val product_name: String,
    val quantity: Int,
    val price_per_item: Double,
    val total_price: Double
)

data class CartResponse(
    val cart_items: List<CartItemApi>,
    val total_cart_price: Double
)