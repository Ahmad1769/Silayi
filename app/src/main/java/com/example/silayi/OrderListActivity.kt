package com.example.silayi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderListActivity : AppCompatActivity(), OrderActionListener {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("orders")

        orderRecyclerView = findViewById(R.id.orderRecyclerView)
        orderAdapter = OrderAdapter(this)

        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        orderRecyclerView.adapter = orderAdapter

        fetchOrdersFromFirebase()
    }
    override fun onOrderAccepted(orderId: String) {
        fetchOrdersFromFirebase()
    }

    override fun onOrderRejected(orderId: String) {
        fetchOrdersFromFirebase()
    }
    private fun fetchOrdersFromFirebase() {
        val currentUser = auth.currentUser
        val currentUserid = currentUser?.uid

        if (currentUserid != null) {
            val userOrdersReference = databaseReference.child(currentUserid)

            userOrdersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val ordersList = mutableListOf<DataSnapshot>()
                        for (orderSnapshot in dataSnapshot.children) {
                            ordersList.add(orderSnapshot)
                        }
                        orderAdapter.setOrders(ordersList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    }
            })
        }
    }
}
