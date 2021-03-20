package shvyn22.marvelapplication.api

data class ApiResponse<T>(
    val code: Int,
    val data: Result<T>
) {
    data class Result<T>(
        val results: List<T>
    )
}

