// D:\android_prj\PersonManagerFront\app\src\main\java\com\example\personmanagerfront\ui\cart\CartApiAdapter.kt
package com.example.personmanagerfront.ui.cart

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
import com.example.personmanagerfront.network.ApiClient
import com.example.personmanagerfront.network.CartItemApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartApiAdapter(
    private val context: Context,
    private var cartItems: MutableList<CartItemApi>
) : RecyclerView.Adapter<CartApiAdapter.CartApiViewHolder>() {

    inner class CartApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textKnifeName: TextView = itemView.findViewById(R.id.textViewKnifeName)
        val textQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartApiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartApiViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartApiViewHolder, position: Int) {
        val item = cartItems[position]
        holder.textKnifeName.text = item.product_name

        val info = "Кол-во: ${item.quantity}\n" +
                "Цена за шт: ${item.price_per_item}\n" +
                "Итого: ${item.total_price}"

        holder.textQuantity.text = info

        // Удаляем по item.id
        holder.buttonRemove.setOnClickListener {
            removeItemFromCart(item.id, position)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    private fun removeItemFromCart(cartItemId: Int, position: Int) {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Токен отсутствует, авторизуйтесь заново.", Toast.LENGTH_SHORT).show()
            return
        }

        val authHeader = "Bearer $token"
        ApiClient.apiService.removeFromCart(authHeader, cartItemId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Товар удален из корзины", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ошибка удаления: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}