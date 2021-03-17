package shvyn22.marvelapplication.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Character")
data class MarvelCharacter(
    @PrimaryKey val id: Int,

    val name: String,

    val description: String,

    val thumbnail: Thumbnail
) : Parcelable
