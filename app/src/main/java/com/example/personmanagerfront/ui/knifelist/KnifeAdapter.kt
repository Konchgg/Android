package com.example.personmanagerfront.ui.knifelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.personmanagerfront.R
import com.example.personmanagerfront.auth.TokenManager
import com.example.personmanagerfront.network.AddToCartRequest
import com.example.personmanagerfront.network.AddToCartResponse
import com.example.personmanagerfront.network.ApiClient
import com.example.personmanagerfront.network.Knife
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KnifeAdapter(
    private val context: Context,
    private val knives: List<Knife>
) : RecyclerView.Adapter<KnifeAdapter.KnifeViewHolder>() {

    inner class KnifeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textViewName)
        val textDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val textStock: TextView = itemView.findViewById(R.id.textViewStock)
        val buttonAddToCart: Button = itemView.findViewById(R.id.buttonAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnifeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_knife, parent, false)
        return KnifeViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnifeViewHolder, position: Int) {
        val knife = knives[position]
        holder.textName.text = knife.name
        holder.textDescription.text = knife.description
        holder.textPrice.text = "Цена: ${knife.price}"
        holder.textStock.text = "В наличии: ${knife.stock}"

        holder.buttonAddToCart.setOnClickListener {
            addToCart(knife.id, 1)
        }
    }

    override fun getItemCount(): Int = knives.size

    private fun addToCart(knifeId: Int, quantity: Int) {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Токен отсутствует. Авторизуйтесь заново.", Toast.LENGTH_SHORT).show()
            return
        }

        val authHeader = "Bearer $token"
        val request = AddToCartRequest(knife_id = knifeId, quantity = quantity)

        ApiClient.apiService.addToCart(authHeader, request).enqueue(object : Callback<AddToCartResponse> {
            override fun onResponse(call: Call<AddToCartResponse>, response: Response<AddToCartResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Toast.makeText(context, "Добавлено в корзину (id=${body.id}, qty=${body.quantity})", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Добавлено (пустой ответ).", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}