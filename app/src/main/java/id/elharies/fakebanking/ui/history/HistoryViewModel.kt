package id.elharies.fakebanking.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.transaction.TransactionRes
import id.elharies.fakebanking.domain.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(private val transactionRepository: TransactionRepository): ViewModel() {

    private val _state: MutableStateFlow<HistoryUiState> = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState>
        get() = _state.asStateFlow()

    fun processAction(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.LoadHistory -> loadTransactions()
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val transactions: ApiResult<List<TransactionRes>> = transactionRepository.getTransactions()) {
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, transactions = listOf()) }
                }
                is ApiResult.Success -> {
                    _state.update { it.copy(transactions = transactions.result, isLoading = false) }
                }
            }
        }
    }
}