package com.example.musicplayerapp.data.player.api.repository

import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.data.player.implementation.mapper.toSong
import com.example.musicplayerapp.data.player.implementation.remote.api.MusicPlayerApi
import com.example.musicplayerapp.data.player.implementation.remote.response.AlbumResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.ArtistResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.SongResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.TrackListResponse
import com.example.musicplayerapp.data.player.implementation.remote.response.TracksResponse
import com.example.musicplayerapp.data.player.implementation.repository.MusicPlayerRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MusicPlayerRepositoryTest {

    private val musicPlayerApi: MusicPlayerApi = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: MusicPlayerRepositoryImpl

    @Before
    fun setUp() {
        repository = MusicPlayerRepositoryImpl(
            musicPlayerApi = musicPlayerApi,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `getMusicList returns Success when API call is successful`(): Unit = runTest {
        // GIVEN
        val fakeTrackResponse = TracksResponse(data = mockSongList)
        val fakeApiResponse = TrackListResponse(tracksResponse = fakeTrackResponse)

        coEvery { musicPlayerApi.getMusicList(any()) } returns Response.success(fakeApiResponse)

        val expectedSongs = mockSongList.map { it.toSong() }

        // WHEN
        val result = repository.getMusicList()

        // THEN
        assertEquals(expectedSongs, (result as ApiResponse.Success).data)
    }

    @Test
    fun `getMusicList returns Error when API call failed`() = runTest {
        // GIVEN
        coEvery { musicPlayerApi.getMusicList(any()) } throws RuntimeException("Network error")

        // WHEN
        val result = repository.getMusicList()
        advanceUntilIdle()

        // THEN
        assert(result is ApiResponse.Error)
        assert((result as ApiResponse.Error).message.contains("Network error"))
    }

    // Mock Data
    private val mockSongResponse = SongResponse(
        album = AlbumResponse(
            cover = "http://api.deezer.com/2.0/album/170781312/image",
            id = 101,
            title = "Album Title"
        ),
        artist = ArtistResponse(
            id = 202,
            name = "Artist Name"
        ),
        duration = 215,
        id = 303,
        preview = "http://www.deezer.com/track/86285553",
        title = "Song Title"
    )

    private val mockSongList = listOf(
        mockSongResponse,
        mockSongResponse.copy(
            id = 304,
            title = "Another Mock Song",
            album = mockSongResponse.album.copy(id = 102, title = "Another Album"),
            artist = mockSongResponse.artist.copy(id = 203, name = "Another Artist")
        )
    )
}