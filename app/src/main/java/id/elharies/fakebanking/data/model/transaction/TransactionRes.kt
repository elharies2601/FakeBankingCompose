package id.elharies.fakebanking.data.model.transaction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRes(
    @SerialName("id")
    val id: String = "",
    @SerialName("date")
    val date: String = "",
    @SerialName("amount")
    val amount: Double = 0.0,
    @SerialName("description")
    val description: String = "",
    @SerialName("accountNumber")
    val accountNumber: String = "",
    @SerialName("status")
    val status: String = ""
)