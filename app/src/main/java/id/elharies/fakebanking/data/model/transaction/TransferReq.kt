package id.elharies.fakebanking.data.model.transaction

data class TransferReq(
    val destinationAccount: String,
    val amount: Double
)
