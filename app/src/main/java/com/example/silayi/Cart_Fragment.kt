package com.example.silayi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.round

class Cart_Fragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var findTailorButton: AppCompatButton
    private lateinit var applyCouponCodeEditText: EditText
    private lateinit var applyButton: AppCompatButton
    private lateinit var subtotalText: TextView
    private lateinit var subtotalAmountText: TextView
    private lateinit var taxTotalText: TextView
    private lateinit var taxAmountText: TextView
    private lateinit var totalText: TextView
    private lateinit var totalAmountText: TextView
    private lateinit var proceedToCheckoutButton: AppCompatButton

    private val cartItems: MutableList<Cartitem> = mutableListOf()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartRecyclerView = view.findViewById(R.id.cart_recycleView)
        findTailorButton = view.findViewById(R.id.findtailor)
        applyCouponCodeEditText = view.findViewById(R.id.applycouponcode)
        applyButton = view.findViewById(R.id.button4)
        subtotalText = view.findViewById(R.id.subtotal)
        subtotalAmountText = view.findViewById(R.id.subtotalamount)
        taxTotalText = view.findViewById(R.id.taxtotal)
        taxAmountText = view.findViewById(R.id.taxamount)
        totalText = view.findViewById(R.id.total)
        totalAmountText = view.findViewById(R.id.totalamount)
        proceedToCheckoutButton = view.findViewById(R.id.proceedtocheckout)

        cartAdapter = CartAdapter(cartItems)
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartRecyclerView.adapter = cartAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("carts").child(userId)
            fetchCartItems()
            proceedToCheckoutButton.setOnClickListener {
                if (cartItems.isNotEmpty()) {
                    startActivity(Intent(context, CheckoutActivity::class.java))
                } else {
                    Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show()
                }
            }

            findTailorButton.setOnClickListener {
                if (cartItems.isNotEmpty()) {
                    startActivity(Intent(context,Tailor_list::class.java))
                } else {
                    Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show()
                }
            }

            applyButton.setOnClickListener {
                Toast.makeText(context, "Apply Button Clicked", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun fetchCartItems() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()

                for (itemSnapshot in snapshot.children) {
                    val itemName = itemSnapshot.key.toString()
                    val quantity = itemSnapshot.child("quantity").getValue(Long::class.java) ?: 0
                    val totalPrice = itemSnapshot.child("totalPrice").getValue(Long::class.java) ?: 0

                    val cartItem = Cartitem(itemName, quantity.toInt(), totalPrice.toInt())
                    cartItems.add(cartItem)
                }

                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun updateUI() {
        cartAdapter.notifyDataSetChanged()

        val subtotal = cartItems.sumBy { it.totalPrice }
        val taxRate = 0.1 // 10% tax rate (you can adjust this)
        val tax = round(subtotal * taxRate).toInt()
        val total = subtotal + tax

        subtotalAmountText.text = "$subtotal"
        taxAmountText.text = "$tax"
        totalAmountText.text = "$total"
    }
}
