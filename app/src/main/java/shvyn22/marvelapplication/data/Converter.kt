package shvyn22.marvelapplication.data

import androidx.room.TypeConverter
import shvyn22.marvelapplication.data.entity.Thumbnail

class Converter {
    @TypeConverter
    fun thumbToString(thumbnail: Thumbnail): String {
        return thumbnail.getFullUrl()
    }

    @TypeConverter
    fun strToThumbnail(str: String): Thumbnail {
        val index = str.lastIndexOf(".")
        return Thumbnail(str.slice(0 until index),
        str.slice(index + 1..str.lastIndex))
    }
}