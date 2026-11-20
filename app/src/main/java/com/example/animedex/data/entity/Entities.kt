package com.example.animedex.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// ---------------------------------------------------------------------
// Tablas principales
// ---------------------------------------------------------------------

@Entity(tableName = "anime", indices = [Index("rank"), Index("type")])
data class AnimeEntity(
    @PrimaryKey @ColumnInfo(name = "anime_id") val animeId: Int,
    val title: String,
    val score: Double,
    val rank: Int,
    val popularity: Int,
    val members: Int,
    val synopsis: String?,
    @ColumnInfo(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "end_date") val endDate: String?,
    val type: String,
    val episodes: Int?,
    @ColumnInfo(name = "image_url") val imageUrl: String
)

@Entity(tableName = "genre")
data class GenreEntity(
    @PrimaryKey @ColumnInfo(name = "genre_id") val genreId: Int,
    val name: String,
    val category: String
)

@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey @ColumnInfo(name = "company_id") val companyId: Int,
    val name: String?,
    @ColumnInfo(name = "company_type") val companyType: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)

// La tabla se llama "character" en SQL; la clase la llamamos CharacterEntity para no chocar con los nombres de Kotlin.
@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey @ColumnInfo(name = "character_id") val characterId: Int,
    val name: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)

@Entity(
    tableName = "person",
    indices = [Index("person_type")]
)
data class PersonEntity(
    @PrimaryKey @ColumnInfo(name = "person_id") val personId: Int,
    val name: String?,
    @ColumnInfo(name = "person_type") val personType: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)

// ---------------------------------------------------------------------
// Tablas de relación
// ---------------------------------------------------------------------

@Entity(
    tableName = "anime_genre",
    primaryKeys = ["anime_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["anime_id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["genre_id"],
            childColumns = ["genre_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("genre_id")]
)
data class AnimeGenreCrossRef(
    @ColumnInfo(name = "anime_id") val animeId: Int,
    @ColumnInfo(name = "genre_id") val genreId: Int
)

@Entity(
    tableName = "anime_company",
    primaryKeys = ["anime_id", "company_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["anime_id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CompanyEntity::class,
            parentColumns = ["company_id"],
            childColumns = ["company_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("company_id")]
)
data class AnimeCompany(
    @ColumnInfo(name = "anime_id") val animeId: Int,
    @ColumnInfo(name = "company_id") val companyId: Int,
    val role: String
)

@Entity(
    tableName = "anime_character",
    primaryKeys = ["anime_id", "character_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["anime_id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["character_id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("character_id")]
)
data class AnimeCharacter(
    @ColumnInfo(name = "anime_id") val animeId: Int,
    @ColumnInfo(name = "character_id") val characterId: Int,
    val role: String
)

@Entity(
    tableName = "anime_staff",
    primaryKeys = ["anime_id", "person_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["anime_id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("person_id")]
)
data class AnimeStaff(
    @ColumnInfo(name = "anime_id") val animeId: Int,
    @ColumnInfo(name = "person_id") val personId: Int,
    val role: String
)

@Entity(
    tableName = "character_voice_actor",
    primaryKeys = ["character_id", "person_id", "language"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["character_id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("person_id")]
)
data class CharacterVoiceActor(
    @ColumnInfo(name = "character_id") val characterId: Int,
    @ColumnInfo(name = "person_id") val personId: Int,
    val language: String
)
