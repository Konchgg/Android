package com.example.personmanagerfront.network

import retrofit2.Call
import retrofit2.http.*

// Интерфейс для взаимодействия с сервером
interface ApiService {

    // Авторизация
    @POST("api/auth/token/")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Обновление токена (необязательно, если вы этим не пользуетесь пока)
    @POST("api/auth/token/refresh/")
    fun refreshAccessToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<LoginResponse>

    // Список ножей
    @GET("api/knives/")
    fun getKnives(
        @Header("Authorization") authHeader: String
    ): Call<List<Knife>>

    // Добавить товар в корзину
    @POST("api/cart/add/")
    fun addToCart(
        @Header("Authorization") authHeader: String,
        @Body addToCartRequest: AddToCartRequest
    ): Call<AddToCartResponse>

    // Удалить товар из корзины
    // DELETE /api/cart/remove/<id>/
    @DELETE("api/cart/remove/{id}/")
    fun removeFromCart(
        @Header("Authorization") authHeader: String,
        @Path("id") cartItemId: Int
    ): Call<Void>

    // Просмотреть корзину
    // Возвращает CartResponse: { "cart_items": [...], "total_cart_price": ... }
    @GET("api/cart/")
    fun getCart(
        @Header("Authorization") authHeader: String
    ): Call<CartResponse>

    // Оформить заказ (JSON)
    @POST("api/checkout/")
    fun checkout(
        @Header("Authorization") authHeader: String,
        @Body checkoutRequest: CheckoutRequest
    ): Call<CheckoutResponse>

    // Получить список заказов (GET /api/orders/)
    @GET("api/orders/")
    fun getOrders(
        @Header("Authorization") authHeader: String
    ): Call<List<OrderApi>>
}