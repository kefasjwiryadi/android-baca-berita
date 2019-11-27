package com.kefasjwiryadi.bacaberita.domain

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "article_table")
data class Article(
    @Embedded val source: Source?,
    var author: String?,
    var title: String?,
    var description: String?,
    @PrimaryKey
    var url: String,
    var urlToImage: String?,
    var publishedAt: String?,
    var content: String?,
    var category: String?,
    var timeRetrieved: Long = System.currentTimeMillis(),
    var onPage: Int = 1,
    var totalResults: Int
) : Parcelable

@Parcelize
data class Source(
    val id: String?,
    val name: String?
) : Parcelable