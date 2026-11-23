package com.example.animedex.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.example.animedex.data.entity.AnimeEntity
import com.example.animedex.data.entity.AnimeGenreCrossRef
import com.example.animedex.data.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

data class AnimeWithGenres(
    @Embedded val anime: AnimeEntity,
    @Relation(
        parentColumn = "anime_id",
        entityColumn = "genre_id",
        associateBy = Junction(
            value = AnimeGenreCrossRef::class,
            parentColumn = "anime_id",
            entityColumn = "genre_id",
        ),
    )
    val genres: List<GenreEntity>,
)

data class CompanyWithAnimeCount(
    val name: String?,
    val count: Int,
)

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime ORDER BY rank LIMIT :limit")
    fun topAnime(limit: Int): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE anime_id = :id")
    fun byId(id: Int): Flow<AnimeEntity?>

    @Transaction
    @Query("SELECT * FROM anime WHERE anime_id = :id")
    fun detail(id: Int): Flow<AnimeWithGenres?>

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :text || '%' ORDER BY rank")
    fun searchByTitle(text: String): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :text || '%' ORDER BY rank")
    fun search(text: String): Flow<List<AnimeEntity>>

    @Query(
        "SELECT anime.* FROM anime " +
        "INNER JOIN anime_genre ON anime.anime_id = anime_genre.anime_id " +
        "INNER JOIN genre ON anime_genre.genre_id = genre.genre_id " +
        "WHERE genre.name = :genreName",
    )
    fun byGenre(genreName: String): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE score >= :minScore")
    fun byMinScore(minScore: Double): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE type = :type")
    fun byType(type: String): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE episodes IS NULL")
    fun withoutEpisodes(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime ORDER BY members DESC LIMIT :limit")
    fun topByMembers(limit: Int = 3): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime ORDER BY members DESC LIMIT 3")
    fun top3ByMembers(): Flow<List<AnimeEntity>>

    @Query(
        "SELECT company.name AS name, COUNT(anime_company.anime_id) AS count " +
        "FROM company " +
        "INNER JOIN anime_company ON company.company_id = anime_company.company_id " +
        "WHERE anime_company.role = 'Studio' " +
        "GROUP BY company.company_id " +
        "ORDER BY count DESC " +
        "LIMIT 1",
    )
    fun topStudio(): Flow<CompanyWithAnimeCount?>

    @Query(
        "SELECT company.name AS name, COUNT(anime_company.anime_id) AS count " +
        "FROM company " +
        "INNER JOIN anime_company ON company.company_id = anime_company.company_id " +
        "WHERE anime_company.role = 'Studio' " +
        "GROUP BY company.company_id " +
        "ORDER BY count DESC " +
        "LIMIT :limit",
    )
    fun topStudiosByAnimeCount(limit: Int = 10): Flow<List<CompanyWithAnimeCount>>
}
