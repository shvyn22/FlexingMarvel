package shvyn22.marvelapplication.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Event")
data class Event(
    @PrimaryKey val id: Int,

    val title: String,

    val description: String,

    val thumbnail: Thumbnail
) : Parcelable
