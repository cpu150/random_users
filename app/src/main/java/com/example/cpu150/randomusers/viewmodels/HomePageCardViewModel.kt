package com.example.cpu150.randomusers.viewmodels

import com.example.cpu150.randomusers.models.RandomUserModel

class HomePageCardViewModel (private val userModel: RandomUserModel) {

    fun getName (longFormatString: String, mediumTitleFormatString: String, mediumFormatString: String, shortFormatString: String, defaultValue: String?): String? {

        val title = userModel.name?.title
        val first = userModel.name?.first
        val last = userModel.name?.last

        return when {
            title != null && first != null && last != null -> String.format(longFormatString, title, first, last)
            first != null && last != null -> String.format(mediumFormatString, first, last)
            title != null && last != null -> String.format(mediumTitleFormatString, title, last)
            first != null -> String.format(shortFormatString, first)
            last != null -> String.format(shortFormatString, last)
            else -> defaultValue
        }
    }

    fun getAvatarUrlString (defaultUrl: String?): String? {

        val large = userModel.picture?.large?.toString()
        val medium = userModel.picture?.medium?.toString()
        val thumbnail = userModel.picture?.thumbnail?.toString()

        return when {
            large != null -> large
            medium != null -> medium
            thumbnail != null -> thumbnail
            else -> defaultUrl
        }
    }
}