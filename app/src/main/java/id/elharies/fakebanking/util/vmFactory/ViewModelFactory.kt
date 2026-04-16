package id.elharies.fakebanking.util.vmFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.elharies.fakebanking.domain.TransactionRepository
import id.elharies.fakebanking.domain.UserRepository
import id.elharies.fakebanking.ui.dashboard.DashboardViewModel
import id.elharies.fakebanking.ui.history.HistoryViewModel
import id.elharies.fakebanking.ui.login.LoginViewModel
import id.elharies.fakebanking.ui.transfer.TransferViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val userRepository: UserRepository, private val transactionRepository: TransactionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel() as T
            }
            modelClass.isAssignableFrom(TransferViewModel::class.java) -> {
                TransferViewModel(transactionRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(transactionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}