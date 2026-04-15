package id.elharies.fakebanking.ui.login

import id.elharies.fakebanking.data.model.user.User

sealed interface LoginEvent {
    data class NavigateToDashboard(val user: User): LoginEvent
    data class ShowError(val message: String): LoginEvent
}