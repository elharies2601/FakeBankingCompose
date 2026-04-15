package id.elharies.fakebanking.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.transaction.TransactionRes
import id.elharies.fakebanking.data.model.transaction.TransferReq
import id.elharies.fakebanking.domain.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(@ApplicationContext val context: Context): TransactionRepository {
    override suspend fun getTransactions(): ApiResult<List<TransactionRes>> {
        return withContext(Dispatchers.IO) {
            try {
                delay(1500L)
                val json = context.assets
                    .open("transactions.json")
                    .bufferedReader()
                    .use { it.readText() }

                val transactions = Json.decodeFromString<List<TransactionRes>>(json)
                ApiResult.Success(transactions)

            } catch (e: Exception) {
                e.printStackTrace()
                ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun transfer(transferReq: TransferReq): ApiResult<String> {
        delay(1500L)
        return if ((1..10).random() <= 8) {
            ApiResult.Success("Berhasil melakukan transfer sebesar ${transferReq.amount} ke ${transferReq.destinationAccount}")
        } else {
            ApiResult.Error("Transfer gagal")
        }
    }
}