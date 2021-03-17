package shvyn22.marvelapplication.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnail(
    val path : String,

    val extension: String
) : Parcelable {
    fun getFullUrl() = "$path.$extension"
}
