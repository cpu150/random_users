package com.example.cpu150.randomusers.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.models.RandomUserModel

class HomePageListAdapter (private val dataList: List <RandomUserModel?>?, private val context: Context) : RecyclerView.Adapter<HomePageListAdapter.HomePageListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomePageListViewHolder {
        // Create new card view
        val cardView = LayoutInflater.from(p0.context).inflate(R.layout.card_view_home_page, p0, false) as ConstraintLayout

        // Set view
        cardView.findViewById<TextView?>(R.id.nameTextView)?.text = getName (dataList?.get(p1))

        return HomePageListViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(p0: HomePageListViewHolder, p1: Int) {
        // Update card
        p0.itemView.findViewById<TextView?>(R.id.nameTextView)?.text = getName (dataList?.get(p1))
    }

    class HomePageListViewHolder(cardView: ConstraintLayout) : RecyclerView.ViewHolder(cardView)

    private fun getName (userModel: RandomUserModel?): String {
        return String.format(
            context.getString(R.string.home_page_card_name),
            userModel?.name?.first ?: "",
            userModel?.name?.last ?: "")
    }
}
