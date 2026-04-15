package id.elharies.fakebanking.domain

import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.transaction.TransactionRes
import id.elharies.fakebanking.data.model.transaction.TransferReq

interface TransactionRepository {
    suspend fun getTransactions(): ApiResult<List<TransactionRes>>
    suspend fun transfer(transferReq: TransferReq): ApiResult<String>
}