package com.example.cpu150.randomusers.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.viewmodels.HomePageCardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_home_page.view.*
import javax.inject.Inject

class HomePageListAdapter @Inject constructor(private val context: Context, private val picasso: Picasso) : RecyclerView.Adapter<HomePageListAdapter.HomePageListViewHolder>() {

    var dataList: List<HomePageCardViewModel>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomePageListViewHolder {
        // Create new card view
        val cardView = LayoutInflater.from(p0.context).inflate(R.layout.card_view_home_page, p0, false) as View

        updateCardView (dataList?.get(p1), cardView)

        return HomePageListViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(p0: HomePageListViewHolder, p1: Int) {
        updateCardView (dataList?.get(p1), p0.itemView)
    }

    class HomePageListViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView)

    private fun updateCardView (cardViewModel: HomePageCardViewModel?, cardView: View) {
        cardViewModel?.also {
            cardView.nameTextView.text = it.getName (
                context.getString(R.string.home_page_card_long_name),
                context.getString(R.string.home_page_card_medium_title_name),
                context.getString(R.string.home_page_card_medium_name),
                context.getString(R.string.home_page_card_short_name),
                context.getString(R.string.home_page_card_default_name))

            picasso
                .load(it.getAvatarUrlString(null))
                .placeholder(R.drawable.tux)
                .into(cardView.avatarImageView)
        }
    }
}
