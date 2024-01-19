package com.example.silayi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


data class Cartitem(val itemName: String, var quantity: Int, var totalPrice: Int)

class CartAdapter(private val cartItems: List<Cartitem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_recycle_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.itemName.text = cartItem.itemName
        holder.cartSize.text = "Size ${cartItem.quantity} meter"
        holder.cartPrice.text = "$${cartItem.totalPrice}"
        holder.addBtn.setOnClickListener {
            cartItem.quantity++
            cartItem.totalPrice =
                (cartItem.quantity*calculateTotalPrice(cartItem.quantity-1,cartItem.totalPrice)).toDouble()
                    .toInt()
            updateUI(holder, cartItem)
            updateFirebase(cartItem)
        }

        holder.subtractBtn.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                cartItem.totalPrice =
                    (cartItem.quantity*calculateTotalPrice(cartItem.quantity+1,cartItem.totalPrice)).toDouble()
                        .toInt()
                updateUI(holder, cartItem)
                updateFirebase(cartItem)
            }
        }

        holder.statusCategory.text = "X"
    }

    private fun updateUI(holder: CartViewHolder, cartItem: Cartitem) {
        holder.qtyBtn.text = cartItem.quantity.toString()
        holder.cartPrice.text = "$${cartItem.totalPrice}"
    }

    private fun updateFirebase(cartItem: Cartitem) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("carts").child(userId)
                    .child(cartItem.itemName)

            databaseReference.child("quantity").setValue(cartItem.quantity)
            databaseReference.child("totalPrice").setValue(cartItem.totalPrice)
        }
    }

    private fun calculateTotalPrice(quantity: Int, price: Int): Int {
        return  price/quantity
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: MaterialTextView = itemView.findViewById(R.id.name_cloth)
        val cartSize: MaterialTextView = itemView.findViewById(R.id.cart_size)
        val cartPrice: MaterialTextView = itemView.findViewById(R.id.cart_price)
        val statusCategory: MaterialTextView = itemView.findViewById(R.id.status_category)
        val addBtn: TextView = itemView.findViewById(R.id.AddBtn)
        val qtyBtn: TextView = itemView.findViewById(R.id.qtyBtn)
        val subtractBtn: TextView = itemView.findViewById(R.id.SubtractBtn)
    }
}

