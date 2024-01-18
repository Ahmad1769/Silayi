package com.example.silayi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home_fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView1: RecyclerView = view.findViewById(R.id.reCycleView)
        val recyclerView2: RecyclerView = view.findViewById(R.id.tailor_recyclerView)
        val recyclerView3: RecyclerView = view.findViewById(R.id.clothes_recyclerView)
        val recyclerView4: RecyclerView = view.findViewById(R.id.recommended_recyclerView)

        val adapterTailor = TailorAdapter(requireContext())
        // val adapter2 = YourAdapter2()
         val adapterCloth = context?.let { ClotheAdapter(it) }
        // val adapter4 = YourAdapter4()

        recyclerView1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView1.adapter = adapterTailor

        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = adapterTailor

        recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = adapterCloth

        recyclerView4.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.adapter = adapterTailor
    }
}
