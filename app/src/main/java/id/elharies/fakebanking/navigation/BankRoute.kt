package id.elharies.fakebanking.navigation

import androidx.navigation3.runtime.NavKey
import id.elharies.fakebanking.data.model.user.User
import kotlinx.serialization.Serializable

sealed interface BankRoute: NavKey {
    @Serializable
    data object Login: BankRoute
    @Serializable
    data class Dashboard(val user: User): BankRoute
    @Serializable
    data class Transfer(val user: User): BankRoute
    @Serializable
    data class History(val user: User): BankRoute
}