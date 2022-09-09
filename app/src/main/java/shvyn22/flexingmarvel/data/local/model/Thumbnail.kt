package shvyn22.flexingmarvel.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val path: String,
    val extension: String
) : Parcelable {

    fun getFullUrl() = "$path.$extension"
}