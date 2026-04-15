package id.elharies.fakebanking.ui.dashboard

interface DashboardEvent {
    data object NavigateToTransfer: DashboardEvent
    data object NavigateToHistory: DashboardEvent
    data object NavigateToLogin: DashboardEvent
}