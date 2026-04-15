package id.elharies.fakebanking.data.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("username")
    val username: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("balance")
    val balance: Double = 0.0,
    @SerialName("accountNumber")
    val accountNumber: String = ""
)