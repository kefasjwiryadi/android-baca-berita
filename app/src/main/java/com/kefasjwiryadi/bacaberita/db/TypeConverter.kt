package com.kefasjwiryadi.bacaberita.db

import androidx.room.TypeConverter

object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun stringListToString(strings: List<String>): String = strings.joinToString()

    @TypeConverter
    @JvmStatic
    fun stringToStringList(string: String): List<String> = string.split(", ")
}