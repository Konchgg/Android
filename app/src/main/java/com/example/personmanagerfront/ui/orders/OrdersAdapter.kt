// D:\android_prj\PersonManagerFront\app\src\main\java\com\example\personmanagerfront\ui\orders\OrdersAdapter.kt
package com.example.personmanagerfront.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personmanagerfront.R
import com.example.personmanagerfront.network.OrderApi

class OrdersAdapter(
    private val orders: List<OrderApi>
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrderTitle: TextView = itemView.findViewById(R.id.textOrderTitle)
        val textOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)
        val textOrderPrice: TextView = itemView.findViewById(R.id.textOrderPrice)
        val textOrderCreated: TextView = itemView.findViewById(R.id.textOrderCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orders[position]
        holder.textOrderTitle.text = "Заказ #${order.id}: ${order.name}"
        holder.textOrderStatus.text = "Статус: ${order.status}"
        holder.textOrderPrice.text = "Сумма: ${order.total_price}"
        holder.textOrderCreated.text = "Создан: ${order.created_at}"
    }

    override fun getItemCount(): Int = orders.size
}