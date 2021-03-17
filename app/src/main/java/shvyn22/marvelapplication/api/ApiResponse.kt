package shvyn22.marvelapplication.api

data class ApiResponse<T>(
    val data: Result<T>
) {
    data class Result<T>(
        val results: List<T>
    )
}

