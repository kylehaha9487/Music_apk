package com.aura.music

import com.aura.music.data.PreloadCatalog
import org.junit.Assert.assertTrue
import org.junit.Test

class AuraSdlcUnitTest {
    @Test
    fun verifyPreloadedCategoriesNotEmpty() {
        assertTrue(PreloadCatalog.categories.size >= 10)
        PreloadCatalog.categories.forEach { cat ->
            val tracks = PreloadCatalog.buildInitialTracksForCategory(cat.key)
            assertTrue("Category ${cat.key} should not be empty", tracks.isNotEmpty())
        }
    }
}
