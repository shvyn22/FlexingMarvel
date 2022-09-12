package shvyn22.flexingmarvel.util

import shvyn22.flexingmarvel.BuildConfig

const val DATASTORE_FILENAME = "preferences"
const val DATABASE_NAME = "appDatabase"

const val BASE_URL = "https://gateway.marvel.com/v1/public/"
const val API_LIMIT = 33
const val API_KEY = BuildConfig.API_KEY
const val API_TS = BuildConfig.API_TS
const val API_HASH = BuildConfig.API_HASH

const val MAX_PAGING_SIZE = API_LIMIT * 3

const val SEARCH_CHARACTERS_KEY = "searchCharacters"
const val SEARCH_EVENTS_KEY = "searchEvents"
const val SEARCH_SERIES_KEY = "searchSeries"