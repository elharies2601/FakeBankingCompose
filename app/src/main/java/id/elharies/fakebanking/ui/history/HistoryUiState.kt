package id.elharies.fakebanking.ui.history

import id.elharies.fakebanking.data.model.transaction.TransactionRes

data class HistoryUiState(
    val transactions: List<TransactionRes> = emptyList(),
    val isLoading: Boolean = false
)
