package shvyn22.marvelapplication.util

sealed class Resource<T>(
    val data: T? = null,
    val msg: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Empty<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Error<T>(msg: String) : Resource<T>(msg = msg)
}