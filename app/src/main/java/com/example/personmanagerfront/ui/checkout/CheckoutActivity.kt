package com.example.personmanagerfront.ui.checkout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personmanagerfront.R
import com.example.personmanagerfront.auth.TokenManager
import com.example.personmanagerfront.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonSubmitOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        editTextName = findViewById(R.id.editTextName)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSubmitOrder = findViewById(R.id.buttonSubmitOrder)

        buttonSubmitOrder.setOnClickListener {
            val name = editTextName.text.toString()
            val address = editTextAddress.text.toString()
            val phone = editTextPhone.text.toString()

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                submitOrder(name, address, phone)
            }
        }
    }

    private fun submitOrder(name: String, address: String, phone: String) {
        val tokenManager = TokenManager(this)
        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Токен отсутствует", Toast.LENGTH_SHORT).show()
            return
        }
        val authHeader = "Bearer $token"
        val request = CheckoutRequest(name, address, phone)

        ApiClient.apiService.checkout(authHeader, request).enqueue(object : Callback<CheckoutResponse> {
            override fun onResponse(call: Call<CheckoutResponse>, response: Response<CheckoutResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Toast.makeText(this@CheckoutActivity, body.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@CheckoutActivity, "Заказ оформлен (пустой ответ)", Toast.LENGTH_LONG).show()
                    }
                    finish()
                } else {
                    Toast.makeText(this@CheckoutActivity, "Ошибка: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}