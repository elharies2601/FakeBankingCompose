package id.elharies.fakebanking.ui.login

sealed interface LoginIntent {
    data class UsernameChanged(val username: String): LoginIntent
    data class PasswordChanged(val password: String): LoginIntent
    data object Submit: LoginIntent
    data object ClearError: LoginIntent
    data object ClearAllField: LoginIntent
}