package id.elharies.fakebanking.ui.history

sealed interface HistoryIntent {
    data object LoadHistory: HistoryIntent
}