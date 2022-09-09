package shvyn22.flexingmarvel.data.local

import androidx.room.TypeConverter
import shvyn22.flexingmarvel.data.local.model.Thumbnail

class Converter {

    @TypeConverter
    fun thumbToString(thumbnail: Thumbnail): String {
        return thumbnail.getFullUrl()
    }

    @TypeConverter
    fun stringToThumbnail(str: String): Thumbnail {
        val index = str.lastIndexOf(".")
        return Thumbnail(
            str.slice(0 until index),
            str.slice(index + 1..str.lastIndex)
        )
    }
}