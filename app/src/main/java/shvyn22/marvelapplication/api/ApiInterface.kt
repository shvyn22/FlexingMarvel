package shvyn22.marvelapplication.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shvyn22.marvelapplication.BuildConfig
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.data.local.model.SeriesModel

interface ApiInterface {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com/v1/public/"
        const val KEY = BuildConfig.API_KEY
        const val LIMIT = 33
        const val TS = BuildConfig.TS
        const val HASH = BuildConfig.HASH
    }

    @GET("characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<CharacterModel>

    @GET("characters")
    suspend fun getCharactersByName(
        @Query("nameStartsWith") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<CharacterModel>

    @GET("characters/{characterId}/series")
    suspend fun getCharacterSeries(
        @Path("characterId") characterId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<SeriesModel>

    @GET("characters/{characterId}/events")
    suspend fun getCharacterEvents(
        @Path("characterId") characterId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<EventModel>

    @GET("series")
    suspend fun getSeries(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<SeriesModel>

    @GET("series")
    suspend fun getSeriesByTitle(
        @Query("titleStartsWith") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<SeriesModel>

    @GET("series/{seriesId}/characters")
    suspend fun getSeriesCharacters(
        @Path("seriesId") seriesId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<CharacterModel>

    @GET("series/{seriesId}/events")
    suspend fun getSeriesEvents(
        @Path("seriesId") seriesId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<EventModel>

    @GET("events")
    suspend fun getEvents(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<EventModel>

    @GET("events")
    suspend fun getEventByName(
        @Query("nameStartsWith") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = LIMIT,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<EventModel>

    @GET("events/{eventId}/characters")
    suspend fun getEventCharacters(
        @Path("eventId") eventId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<CharacterModel>

    @GET("events/{eventId}/series")
    suspend fun getEventSeries(
        @Path("eventId") eventId: Int,
        @Query("ts") ts: String = TS,
        @Query("apikey") apiKey: String = KEY,
        @Query("hash") hash: String = HASH
    ): ApiResponse<SeriesModel>
}