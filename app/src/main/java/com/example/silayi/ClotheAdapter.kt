package com.example.silayi

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ClotheAdapter(private val context: Context) :
    RecyclerView.Adapter<ClotheAdapter.ClotheViewHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("clothes")
    private val clotheList: MutableList<Clothe> = mutableListOf()

    init {
        // Fetch data from Firebase and populate the list
        fetchDataFromFirebase()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClotheViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_list_layout, parent, false)
        return ClotheViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClotheViewHolder, position: Int) {
        val clothe = clotheList[position]
        holder.name.text = clothe.name
        holder.price.text = clothe.price.toString()
        holder.description.text = clothe.description
        holder.image.setImageResource(clothe.imageResId)
        holder.image.setBackgroundResource(R.drawable.profile_circle)

        holder.cardView.setOnClickListener {
            val intent = Intent(context, Clothes::class.java)
            intent.putExtra("clotheName", clothe.name)
            intent.putExtra("clothePrice", clothe.price)
            intent.putExtra("clotheDescription", clothe.description)
            context.startActivity(intent)
        }
    }

    private fun fetchDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                clotheList.clear()
                for (clotheSnapshot in dataSnapshot.children) {
                    val name = clotheSnapshot.key ?: ""
                    val price = clotheSnapshot.child("price").getValue(Int::class.java) ?: 0
                    val description = clotheSnapshot.child("description").getValue(String::class.java) ?: ""
                    val imageResId = R.drawable.clothes // Replace with actual drawable ID

                    val clothe = Clothe(name, price, description, imageResId)
                    clotheList.add(clothe)
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ClotheAdapter", "Failed to read value.", databaseError.toException())
            }
        })
    }

    override fun getItemCount(): Int = clotheList.size

    inner class ClotheViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val description: TextView = itemView.findViewById(R.id.descr)
        val cardView: CardView = itemView.findViewById(R.id.card)
    }

    data class Clothe(
        val name: String,
        val price: Int,
        val description: String,
        val imageResId: Int
    )
}
