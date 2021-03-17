package shvyn22.marvelapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import shvyn22.marvelapplication.data.dao.CharacterDao
import shvyn22.marvelapplication.data.dao.EventDao
import shvyn22.marvelapplication.data.dao.SeriesDao
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.data.entity.MarvelCharacter
import shvyn22.marvelapplication.data.entity.Series

@Database(entities = [MarvelCharacter::class, Event::class, Series::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao() : CharacterDao

    abstract fun eventDao() : EventDao

    abstract fun seriesDao() : SeriesDao
}