package com.example.cpu150.randomusers.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.models.RandomUserModel
import com.example.cpu150.randomusers.network.RandomUserApi
import kotlinx.android.synthetic.main.card_view_home_page.view.*

class HomePageListAdapter (private val dataList: List <RandomUserModel?>?, private val context: Context) : RecyclerView.Adapter<HomePageListAdapter.HomePageListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomePageListViewHolder {
        // Create new card view
        val cardView = LayoutInflater.from(p0.context).inflate(R.layout.card_view_home_page, p0, false) as ConstraintLayout

        // Setup card
        dataList?.get(p1)?.let {
            cardView.nameTextView.text = getName (it)
            updateAvatar(it, cardView.avatarImageView)
        }

        return HomePageListViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(p0: HomePageListViewHolder, p1: Int) {
        // Update card
        p0.itemView.nameTextView.text = getName (dataList?.get(p1))
        updateAvatar(dataList?.get(p1), p0.itemView.avatarImageView)
    }

    class HomePageListViewHolder(cardView: ConstraintLayout) : RecyclerView.ViewHolder(cardView)

    private fun getName (userModel: RandomUserModel?): String {
        return String.format(
            context.getString(R.string.home_page_card_name),
            userModel?.name?.first ?: "",
            userModel?.name?.last ?: "")
    }

    private fun updateAvatar (userModel: RandomUserModel?, avatarImageView: ImageView) {
        RandomUserApi
            .getPicasso(context)
            ?.load(userModel?.picture?.large?.toString())
            ?.placeholder(R.drawable.tux)
            ?.into(avatarImageView)
    }
}
