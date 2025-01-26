// D:\android_prj\PersonManagerFront\app\src\main\java\com\example\personmanagerfront\MainActivity.kt
package com.example.personmanagerfront

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personmanagerfront.auth.TokenManager
import com.example.personmanagerfront.network.ApiClient
import com.example.personmanagerfront.network.Knife
import com.example.personmanagerfront.ui.cart.CartActivity
import com.example.personmanagerfront.ui.knifelist.KnifeAdapter
import com.example.personmanagerfront.ui.login.LoginActivity
import com.example.personmanagerfront.ui.orders.OrdersActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var recyclerViewKnives: RecyclerView
    private lateinit var knifeAdapter: KnifeAdapter
    private lateinit var buttonViewCart: Button
    private lateinit var buttonViewOrders: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this)
        val accessToken = tokenManager.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            // Нет токена — на экран логина
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)

            recyclerViewKnives = findViewById(R.id.recyclerViewKnives)
            buttonViewCart = findViewById(R.id.buttonViewCart)
            buttonViewOrders = findViewById(R.id.buttonViewOrders)

            recyclerViewKnives.layoutManager = LinearLayoutManager(this)

            fetchKnives(accessToken)

            // Переход в корзину
            buttonViewCart.setOnClickListener {
                startActivity(Intent(this, CartActivity::class.java))
            }

            // Переход к списку заказов
            buttonViewOrders.setOnClickListener {
                startActivity(Intent(this, OrdersActivity::class.java))
            }
        }
    }

    private fun fetchKnives(token: String) {
        val authHeader = "Bearer $token"
        ApiClient.apiService.getKnives(authHeader).enqueue(object : Callback<List<Knife>> {
            override fun onResponse(call: Call<List<Knife>>, response: Response<List<Knife>>) {
                if (response.isSuccessful) {
                    val knives = response.body()
                    if (knives != null) {
                        knifeAdapter = KnifeAdapter(this@MainActivity, knives)
                        recyclerViewKnives.adapter = knifeAdapter
                    } else {
                        Toast.makeText(this@MainActivity, "Нет товаров.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(this@MainActivity, "Сессия истекла, войдите снова.", Toast.LENGTH_SHORT).show()
                        tokenManager.clearTokens()
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Knife>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}