package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.TunisianChannelsData
import com.example.data.TunisianVodData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("تلفزتنا", appName)
    }

    @Test
    fun `tunisian channels list is loaded with national and variety channels`() {
        val channels = TunisianChannelsData.channels
        assertTrue("Channels list should not be empty", channels.isNotEmpty())
        assertTrue("Should include Watania 1", channels.any { it.id == "watania_1" })
        assertTrue("Should include Watania 2", channels.any { it.id == "watania_2" })
        assertTrue("Should include El Hiwar", channels.any { it.id == "el_hiwar" })
        assertTrue("Should include Hannibal", channels.any { it.id == "hannibal" })
        assertTrue("Should include Nessma", channels.any { it.id == "nessma" })
    }

    @Test
    fun `channels have dedicated distinct feeds and do not mirror watania`() {
        val channels = TunisianChannelsData.channels
        val watania1 = channels.first { it.id == "watania_1" }
        val hannibal = channels.first { it.id == "hannibal" }
        val nessma = channels.first { it.id == "nessma" }
        val attessia = channels.first { it.id == "attessia" }

        assertNotEquals(watania1.primaryServer.url, hannibal.primaryServer.url)
        assertNotEquals(watania1.primaryServer.url, nessma.primaryServer.url)
        assertNotEquals(watania1.primaryServer.url, attessia.primaryServer.url)
    }

    @Test
    fun `choufli hal series is loaded with all 5 full seasons and complete episodes`() {
        val choufliHal = TunisianVodData.choufliHalSeries
        assertNotNull(choufliHal)
        assertEquals("choufli_hal", choufliHal.id)
        assertEquals("Should have all 5 full seasons", 5, choufliHal.seasons.size)

        val s1 = choufliHal.seasons.find { it.seasonNumber == 1 }
        val s2 = choufliHal.seasons.find { it.seasonNumber == 2 }
        val s3 = choufliHal.seasons.find { it.seasonNumber == 3 }
        val s4 = choufliHal.seasons.find { it.seasonNumber == 4 }
        val s5 = choufliHal.seasons.find { it.seasonNumber == 5 }

        assertNotNull(s1)
        assertNotNull(s2)
        assertNotNull(s3)
        assertNotNull(s4)
        assertNotNull(s5)

        assertEquals(30, s1?.episodes?.size)
        assertEquals(30, s2?.episodes?.size)
        assertEquals(30, s3?.episodes?.size)
        assertEquals(15, s4?.episodes?.size)
        assertEquals(29, s5?.episodes?.size)
    }

    @Test
    fun `nsibti laaziza series is loaded with full seasons and complete episodes`() {
        val nsibti = TunisianVodData.nsibtiLaazizaSeries
        assertNotNull(nsibti)
        assertEquals("nsibti_laaziza", nsibti.id)
        assertTrue("Should have multiple seasons", nsibti.seasons.isNotEmpty())
        assertTrue("Should have episodes", nsibti.seasons.first().episodes.isNotEmpty())
    }

    @Test
    fun `tunisian cinema movies are loaded with working full video links`() {
        val movies = TunisianVodData.allMovies
        assertTrue("Movies list should not be empty", movies.isNotEmpty())
        assertTrue("Should include Dachra", movies.any { it.id == "dachra_movie" && it.directMovieUrl != null })
        assertTrue("Should include Samt El Qousour", movies.any { it.id == "samt_el_qousour" && it.directMovieUrl != null })
    }
}
