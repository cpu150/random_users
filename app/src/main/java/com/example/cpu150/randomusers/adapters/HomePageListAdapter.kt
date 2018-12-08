package com.example.cpu150.randomusers.adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.cpu150.randomusers.R

class HomePageListAdapter (private val data: Array<String>) : RecyclerView.Adapter<HomePageListAdapter.HomePageListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomePageListViewHolder {
        // Create new card view
        val cardView = LayoutInflater.from(p0.context).inflate(R.layout.card_view_home_page, p0, false) as ConstraintLayout

        // Set view
        cardView.findViewById<TextView?>(R.id.nameTextView)?.text = data[p1]

        return HomePageListViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: HomePageListViewHolder, p1: Int) {
        // Update card
        p0.itemView.findViewById<TextView?>(R.id.nameTextView)?.text = data[p1]
    }

    class HomePageListViewHolder(cardView: ConstraintLayout) : RecyclerView.ViewHolder(cardView)
}
