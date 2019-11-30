package com.kefasjwiryadi.bacaberita.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kefasjwiryadi.bacaberita.db.TypeConverter

@Entity
@TypeConverters(TypeConverter::class)
data class ArticleFetchResult(
    @PrimaryKey
    val category: String,
    val articleUrls: List<String>,
    val totalResult: Int,
    val page: Int,
    val firstRetrieved: Long = System.currentTimeMillis(),
    val lastRetrieved: Long = System.currentTimeMillis()
)