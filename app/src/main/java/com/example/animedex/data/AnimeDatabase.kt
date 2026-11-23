package com.example.animedex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.animedex.data.dao.AnimeDao
import com.example.animedex.data.entity.AnimeCharacter
import com.example.animedex.data.entity.AnimeCompany
import com.example.animedex.data.entity.AnimeEntity
import com.example.animedex.data.entity.AnimeGenreCrossRef
import com.example.animedex.data.entity.AnimeStaff
import com.example.animedex.data.entity.CharacterEntity
import com.example.animedex.data.entity.CharacterVoiceActor
import com.example.animedex.data.entity.CompanyEntity
import com.example.animedex.data.entity.GenreEntity
import com.example.animedex.data.entity.PersonEntity

@Database(
    entities = [
        AnimeEntity::class,
        GenreEntity::class,
        CompanyEntity::class,
        CharacterEntity::class,
        PersonEntity::class,
        AnimeGenreCrossRef::class,
        AnimeCompany::class,
        AnimeCharacter::class,
        AnimeStaff::class,
        CharacterVoiceActor::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile
        private var instance: AnimeDatabase? = null

        fun get(context: Context): AnimeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime.db",
                )
                    .createFromAsset("databases/anime.db")
                    .build()
                    .also { instance = it }
            }
    }
}

typealias AppDatabase = AnimeDatabase
