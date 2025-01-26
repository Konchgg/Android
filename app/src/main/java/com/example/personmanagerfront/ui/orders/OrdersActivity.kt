// D:\android_prj\PersonManagerFront\app\src\main\java\com\example\personmanagerfront\ui\orders\OrdersActivity.kt
package com.example.personmanagerfront.ui.orders

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personmanagerfront.R
import com.example.personmanagerfront.auth.TokenManager
import com.example.personmanagerfront.network.ApiClient
import com.example.personmanagerfront.network.OrderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersActivity : AppCompatActivity() {

    private lateinit var recyclerViewOrders: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders)
        recyclerViewOrders.layoutManager = LinearLayoutManager(this)

        loadOrders()
    }

    private fun loadOrders() {
        val tokenManager = TokenManager(this)
        val token = tokenManager.getAccessToken()

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Токен отсутствует. Авторизуйтесь заново.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val authHeader = "Bearer $token"
        ApiClient.apiService.getOrders(authHeader).enqueue(object : Callback<List<OrderApi>> {
            override fun onResponse(call: Call<List<OrderApi>>, response: Response<List<OrderApi>>) {
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        ordersAdapter = OrdersAdapter(orders)
                        recyclerViewOrders.adapter = ordersAdapter
                    } else {
                        Toast.makeText(this@OrdersActivity, "Заказы отсутствуют", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OrdersActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderApi>>, t: Throwable) {
                Toast.makeText(this@OrdersActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}