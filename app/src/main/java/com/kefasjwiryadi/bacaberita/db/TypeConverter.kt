package com.kefasjwiryadi.bacaberita.db

import androidx.room.TypeConverter

/**
 * Type converters to allow Room to reference complex data types.
 */
object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun stringListToString(strings: List<String>): String = strings.joinToString()

    @TypeConverter
    @JvmStatic
    fun stringToStringList(string: String): List<String> = string.split(", ")
}