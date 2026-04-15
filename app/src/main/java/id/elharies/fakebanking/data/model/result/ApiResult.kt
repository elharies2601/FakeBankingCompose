package id.elharies.fakebanking.data.model.result

sealed interface ApiResult<out T> {
    data class Success<T>(val result: T): ApiResult<T>
    data class Error(val message: String): ApiResult<Nothing>
}