package com.example.silayi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class tailor_adap : RecyclerView.Adapter<tailor_adap.TailorViewHolder>() {

    data class User(
        val id: String = "",
        val username: String = "",
        val phoneNumber: String = "",
        val isTailor: Boolean = false
    )

    private var tailors: List<User> = mutableListOf()

    fun setTailors(tailors: List<User>) {
        this.tailors = tailors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TailorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recommended_list_layout, parent, false)
        return TailorViewHolder(view)
    }

    override fun onBindViewHolder(holder: TailorViewHolder, position: Int) {
        val tailor = tailors[position]
        holder.bind(tailor)
    }

    override fun getItemCount(): Int = tailors.size

    inner class TailorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val priceTextView: TextView = itemView.findViewById(R.id.price)
        private val descrTextView: TextView = itemView.findViewById(R.id.descr)

        fun bind(tailor: User) {
            nameTextView.text = tailor.username
            priceTextView.text = "Rs 15,000"
            descrTextView.text = tailor.phoneNumber

            itemView.setOnClickListener {
                // Get the user ID of the logged-in user
                val currentUserID = Firebase.auth.currentUser?.uid

                // Check if the user is logged in
                if (currentUserID != null) {
                    // Create a new order node in Firebase with the tailor's user ID as the parent node
                    val orderReference = FirebaseDatabase.getInstance().getReference("orders").child(tailor.id).child(currentUserID)

                    // Fetch cart data from the user's data in Firebase
                    val cartReference = FirebaseDatabase.getInstance().getReference("carts").child(currentUserID)
                    cartReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Check if the cart is not empty
                            if (snapshot.exists()) {
                                // Iterate through each item in the cart
                                for (cartItemSnapshot in snapshot.children) {
                                    // Get the item details
                                    val itemName = cartItemSnapshot.key.toString()
                                    val quantity = cartItemSnapshot.child("quantity").getValue(Long::class.java) ?: 0
                                    val totalPrice = cartItemSnapshot.child("totalPrice").getValue(Long::class.java) ?: 0

                                    // Create a map to store the order details
                                    val orderDetails = mapOf(
                                        "itemName" to itemName,
                                        "quantity" to quantity,
                                        "totalPrice" to totalPrice
                                    )

                                    // Save the order details to the order node
                                    orderReference.child(itemName).setValue(orderDetails)
                                }

                                // Notify the user that the order has been placed
                                Toast.makeText(itemView.context, "Order placed successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                // Notify the user that the cart is empty
                                Toast.makeText(itemView.context, "Cart is empty", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                            Toast.makeText(itemView.context, "Failed to fetch cart data", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    // Handle the case where the user is not logged in
                    Toast.makeText(itemView.context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
