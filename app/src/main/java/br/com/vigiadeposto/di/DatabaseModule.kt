package br.com.vigiadeposto.di

import android.content.Context
import androidx.room.Room
import br.com.vigiadeposto.data.local.PostDao
import br.com.vigiadeposto.data.local.VigiaDePostoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVigiaDePostoDatabase(
        @ApplicationContext context: Context
    ): VigiaDePostoDatabase {
        return Room.databaseBuilder(
            context,
            VigiaDePostoDatabase::class.java,
            "vigia_de_posto_database"
        ).build()
    }

    @Provides
    fun providePostDao(database: VigiaDePostoDatabase): PostDao {
        return database.postDao()
    }
}
