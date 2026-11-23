package com.example.animedex

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.animedex.data.AnimeDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimeDaoTest {
    @Test
    fun gintamaTiene8Generos() = runBlocking {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(ctx, AnimeDatabase::class.java, "test.db")
            .createFromAsset("databases/anime.db")
            .build()
        val ficha = db.animeDao().detail(28977).first()
        assertEquals(8, ficha?.genres?.size)
        db.close()
    }
}
