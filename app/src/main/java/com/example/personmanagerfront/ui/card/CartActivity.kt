// CartActivity.kt
package com.example.personmanagerfront.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personmanagerfront.R
import com.example.personmanagerfront.auth.TokenManager
import com.example.personmanagerfront.network.ApiClient
import com.example.personmanagerfront.network.CartResponse
import com.example.personmanagerfront.ui.checkout.CheckoutActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonCheckout: Button
    private lateinit var cartAdapter: CartApiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonCheckout = findViewById(R.id.buttonCheckout)

        recyclerViewCart.layoutManager = LinearLayoutManager(this)

        // Первоначальная загрузка корзины
        loadCart()

        // Переход к оформлению заказа
        buttonCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    // Когда возвращаемся на экран корзины — обновляем список
    override fun onResume() {
        super.onResume()
        loadCart()
    }

    private fun loadCart() {
        val tokenManager = TokenManager(this)
        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Токен отсутствует, авторизуйтесь заново.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val authHeader = "Bearer $token"

        ApiClient.apiService.getCart(authHeader).enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    val cartResponse = response.body()
                    if (cartResponse != null) {
                        val items = cartResponse.cart_items
                        cartAdapter = CartApiAdapter(this@CartActivity, items.toMutableList())
                        recyclerViewCart.adapter = cartAdapter

                        textViewTotalPrice.text = "Общая сумма: ${cartResponse.total_cart_price}"
                    } else {
                        // Если ответа нет — корзина пуста
                        Toast.makeText(this@CartActivity, "Корзина пуста", Toast.LENGTH_SHORT).show()
                        recyclerViewCart.adapter = null // очистим адаптер
                        textViewTotalPrice.text = "Общая сумма: 0"
                    }
                } else {
                    Toast.makeText(this@CartActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}