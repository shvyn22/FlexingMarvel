package shvyn22.flexingmarvel.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel

interface ApiService {

    @GET("characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
    ): ApiResponse<CharacterModel>

    @GET("characters")
    suspend fun getCharactersByName(
        @Query("nameStartsWith") query: String,
        @Query("offset") offset: Int,
    ): ApiResponse<CharacterModel>

    @GET("characters/{characterId}/series")
    suspend fun getCharacterSeries(
        @Path("characterId") characterId: Int,
    ): ApiResponse<SeriesModel>

    @GET("characters/{characterId}/events")
    suspend fun getCharacterEvents(
        @Path("characterId") characterId: Int,
    ): ApiResponse<EventModel>

    @GET("series")
    suspend fun getSeries(
        @Query("offset") offset: Int,
    ): ApiResponse<SeriesModel>

    @GET("series")
    suspend fun getSeriesByTitle(
        @Query("titleStartsWith") query: String,
        @Query("offset") offset: Int,
    ): ApiResponse<SeriesModel>

    @GET("series/{seriesId}/characters")
    suspend fun getSeriesCharacters(
        @Path("seriesId") seriesId: Int,
    ): ApiResponse<CharacterModel>

    @GET("series/{seriesId}/events")
    suspend fun getSeriesEvents(
        @Path("seriesId") seriesId: Int,
    ): ApiResponse<EventModel>

    @GET("events")
    suspend fun getEvents(
        @Query("offset") offset: Int,
    ): ApiResponse<EventModel>

    @GET("events")
    suspend fun getEventByName(
        @Query("nameStartsWith") query: String,
        @Query("offset") offset: Int,
    ): ApiResponse<EventModel>

    @GET("events/{eventId}/characters")
    suspend fun getEventCharacters(
        @Path("eventId") eventId: Int,
    ): ApiResponse<CharacterModel>

    @GET("events/{eventId}/series")
    suspend fun getEventSeries(
        @Path("eventId") eventId: Int,
    ): ApiResponse<SeriesModel>
}