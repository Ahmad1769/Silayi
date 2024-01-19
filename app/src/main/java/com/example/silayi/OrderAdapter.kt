package com.example.silayi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private var orders: List<DataSnapshot> = mutableListOf()

    fun setOrders(orders: List<DataSnapshot>) {
        this.orders = orders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("orders").child(currentUser.uid)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_row, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderSnapshot = orders[position]
        holder.bind(orderSnapshot)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val customerNameTextView: TextView = itemView.findViewById(R.id.customerNameTextView)
        private val orderPriceTextView: TextView = itemView.findViewById(R.id.orderPriceTextView)
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        private val rejectButton: Button = itemView.findViewById(R.id.rejectButton)

        fun bind(orderSnapshot: DataSnapshot) {
            val orderId = orderSnapshot.key.toString()

            val userReference = FirebaseDatabase.getInstance().getReference("users").child(orderId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {
                        val customerName = userSnapshot.child("username").getValue(String::class.java) ?: "Unknown"
                        customerNameTextView.text = customerName

                        val orderItems = orderSnapshot.children
                        val orderDetails = StringBuilder()

                        for (item in orderItems) {
                            val itemName = item.child("itemName").getValue(String::class.java) ?: "Unknown"
                            val quantity = item.child("quantity").getValue(Int::class.java) ?: 0
                            val totalPrice = item.child("totalPrice").getValue(Double::class.java) ?: 0.0

                            orderDetails.append("Item: $itemName, Quantity: $quantity, Price: $totalPrice\n")
                        }

                        orderPriceTextView.text = orderDetails.toString()

                        acceptButton.setOnClickListener {
                            showToast("Order Accepted: $orderId")

                            databaseReference.child(orderId).removeValue()
                        }

                        rejectButton.setOnClickListener {
                            showToast("Order Rejected: $orderId")

                            databaseReference.child(orderId).removeValue()
                        }
                    } else {
                        showToast("User data not found for order: $orderId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Error fetching user data: ${error.message}")
                }
            })
        }

        private fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
