package br.com.vigiadeposto.di

import br.com.vigiadeposto.data.repository.FirebaseRepository as FirebaseRepositoryImpl
import br.com.vigiadeposto.data.repository.PostRepositoryImpl
import br.com.vigiadeposto.domain.repository.FirebaseRepository
import br.com.vigiadeposto.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(
        firebaseRepositoryImpl: FirebaseRepositoryImpl
    ): FirebaseRepository
}
