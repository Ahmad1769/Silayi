package com.example.silayi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.silayi.R

class CartItem{

}
class CartAdapter(private val cartItems: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycle_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: TextView = itemView.findViewById(R.id.image_cloth)
        private val itemName: TextView = itemView.findViewById(R.id.name_cloth)
        private val itemSize: TextView = itemView.findViewById(R.id.cart_size)
        private val itemCategory: TextView = itemView.findViewById(R.id.status_category)
        private val itemPrice: TextView = itemView.findViewById(R.id.cart_price)

        fun bind(cartItem: CartItem) {
            // itemImage.s
            itemName.text = cartItem.name
            itemSize.text = cartItem.size
            itemCategory.text = cartItem.category
            itemPrice.text = cartItem.price

            // Add any other binding logic you need
        }
    }
}
