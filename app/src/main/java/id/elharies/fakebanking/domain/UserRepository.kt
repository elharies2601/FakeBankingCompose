package id.elharies.fakebanking.domain

import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.user.User

interface UserRepository {
    suspend fun login(username: String, password: String): ApiResult<User>
}