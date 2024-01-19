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

    private val tailorList = listOf(
        Tailor("Ahmad ",5000, R.drawable.profile),
        Tailor("Rafay",6790,R.drawable.profile),
        Tailor("Hameed",1251,R.drawable.profile),
        Tailor("Ali",9511, R.drawable.profile),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TailorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recommended_list_layout, parent, false)
        return TailorViewHolder(view)
    }

    override fun onBindViewHolder(holder: TailorViewHolder, position: Int) {
        val tailor = tailorList[position]

        holder.name.text = tailor.name
        holder.price.text = tailor.price.toString()
        holder.image.setImageResource(tailor.imageurl)

        holder.cardView.setOnClickListener {
            val intent = Intent(context, HomeScreen::class.java)
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
        val imageurl: Int
    )
}
