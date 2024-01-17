package com.example.silayi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TailorAdapter(private val context: Context) :
    RecyclerView.Adapter<TailorAdapter.TailorViewHolder>() {

    // Sample data, replace with your actual data fetched from Firebase
    private val tailorList = listOf(
        Tailor("Android Khan",5, "image"),
        Tailor("Android Khan",5, "image"),
        Tailor("Android Khan",5, "image"),
        Tailor("Android Khan",5, "image"),
        // Add more tailor data as needed
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TailorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recommended_list_layout, parent, false)
        return TailorViewHolder(view)
    }

    override fun onBindViewHolder(holder: TailorViewHolder, position: Int) {
        val tailor = tailorList[position]

        // Set data to views
        holder.name.text = tailor.name
        holder.price.text = tailor.price.toString()
        holder.image.setImageResource(R.mipmap.ic_launcher)

        // Set click listener
        holder.cardView.setOnClickListener {
            // Handle item click, e.g., open another activity
            val intent = Intent(context, HomeScreen::class.java)
            // Add any necessary data to the intent
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tailorList.size

    inner class TailorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val cardView: CardView = itemView.findViewById(R.id.card)
    }

    data class Tailor(
        val name: String,
        val price: Int,
        val imageurl: String
    )
}
