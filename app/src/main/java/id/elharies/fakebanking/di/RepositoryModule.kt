package id.elharies.fakebanking.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.elharies.fakebanking.data.repository.TransactionRepositoryImpl
import id.elharies.fakebanking.data.repository.UserRepositoryImpl
import id.elharies.fakebanking.domain.TransactionRepository
import id.elharies.fakebanking.domain.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
}