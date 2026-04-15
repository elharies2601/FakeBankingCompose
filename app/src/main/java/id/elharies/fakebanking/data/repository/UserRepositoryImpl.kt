package id.elharies.fakebanking.data.repository

import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.user.User
import id.elharies.fakebanking.domain.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {

    private val dummyUsers = mapOf(
        "user123" to Pair("password123", User("user123", "Budi Santoso", 15750000.0, "1234567890")),
        "admin" to Pair("admin123", User("admin", "Admin Utama", 99999999.0, "0000000001")),
        "test" to Pair("test123", User("test", "Test User", 5000000.0, "9876543210"))
    )

    override suspend fun login(username: String, password: String): ApiResult<User> {
        delay(1000L)
        val userFound = dummyUsers[username]
        return if (userFound != null && userFound.first == password) {
            ApiResult.Success(userFound.second)
        } else {
            ApiResult.Error("Username atau password salah")
        }
    }
}