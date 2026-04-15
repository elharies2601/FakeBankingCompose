package id.elharies.fakebanking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import id.elharies.fakebanking.ui.dashboard.DashboardScreen
import id.elharies.fakebanking.ui.dashboard.DashboardViewModel
import id.elharies.fakebanking.ui.history.HistoryScreen
import id.elharies.fakebanking.ui.login.LoginScreen
import id.elharies.fakebanking.ui.login.LoginViewModel
import id.elharies.fakebanking.ui.transfer.TransferScreen
import id.elharies.fakebanking.ui.transfer.TransferViewModel

@Composable
fun BankNavigation(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(BankRoute.Login)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<BankRoute.Login> {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(viewModel = viewModel) { user ->
                    backStack.clear()
                    backStack.add(BankRoute.Dashboard(user))
                }
            }
            entry<BankRoute.Dashboard> {
                val user = it.user
                val viewModel = hiltViewModel<DashboardViewModel>()
                DashboardScreen(
                    viewModel = viewModel,
                    user = user,
                    oNavigateToHistory = {
                        backStack.add(BankRoute.History(user))
                    },
                    oNavigateToTransfer = {
                        backStack.add(BankRoute.Transfer(user))
                    },
                    oNavigateToLogin = {
                        backStack.clear()
                        backStack.add(BankRoute.Login)
                    },)
            }
            entry<BankRoute.Transfer> {
                val user = it.user
                val viewModel = hiltViewModel<TransferViewModel>()
                TransferScreen(
                    viewModel = viewModel,
                    user = user,
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<BankRoute.History> {
                HistoryScreen(onBack = {
                    backStack.removeLastOrNull()
                })
            }
        }
    )
}
