package shvyn22.flexingmarvel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import shvyn22.flexingmarvel.data.local.dao.CharacterDao
import shvyn22.flexingmarvel.data.local.dao.EventDao
import shvyn22.flexingmarvel.data.local.dao.SeriesDao
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel

@Database(
    entities = [
        CharacterModel::class,
        EventModel::class,
        SeriesModel::class
    ],
    version = 1
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun eventDao(): EventDao

    abstract fun seriesDao(): SeriesDao
}