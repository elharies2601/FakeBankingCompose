package id.elharies.fakebanking.ui.dashboard

sealed interface DashboardIntent {
    data object TransferClicked: DashboardIntent
    data object HistoryClicked: DashboardIntent
    data object LogoutClicked: DashboardIntent
}