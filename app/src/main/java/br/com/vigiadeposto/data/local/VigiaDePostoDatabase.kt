package br.com.vigiadeposto.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VigiaDePostoDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
