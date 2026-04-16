package id.elharies.fakebanking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import id.elharies.fakebanking.ui.dashboard.DashboardScreen
import id.elharies.fakebanking.ui.dashboard.DashboardViewModel
import id.elharies.fakebanking.ui.history.HistoryScreen
import id.elharies.fakebanking.ui.history.HistoryViewModel
import id.elharies.fakebanking.ui.login.LoginScreen
import id.elharies.fakebanking.ui.login.LoginViewModel
import id.elharies.fakebanking.ui.transfer.TransferScreen
import id.elharies.fakebanking.ui.transfer.TransferViewModel
import id.elharies.fakebanking.util.vmFactory.ViewModelFactory

@Composable
fun BankNavigation(
    modifier: Modifier = Modifier,
    vmFactory: ViewModelFactory
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
                val viewModel = viewModel<LoginViewModel>(factory = vmFactory)
                LoginScreen(viewModel = viewModel) { user ->
                    backStack.clear()
                    backStack.add(BankRoute.Dashboard(user))
                }
            }
            entry<BankRoute.Dashboard> {
                val user = it.user
                val viewModel = viewModel<DashboardViewModel>(factory = vmFactory)
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
                val viewModel = viewModel<TransferViewModel>(factory = vmFactory)
                TransferScreen(
                    viewModel = viewModel,
                    user = user,
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<BankRoute.History> {
                val viewModel = viewModel<HistoryViewModel>(factory = vmFactory)
                HistoryScreen(viewModel = viewModel, onBack = {
                    backStack.removeLastOrNull()
                })
            }
        }
    )
}
