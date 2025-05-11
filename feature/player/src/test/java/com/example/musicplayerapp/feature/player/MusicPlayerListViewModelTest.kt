package com.example.musicplayerapp.feature.player

import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.data.player.api.model.Album
import com.example.musicplayerapp.data.player.api.model.Artist
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.data.player.api.repository.MusicPlayerRepository
import com.example.musicplayerapp.feature.player.ui.musicplayer.MusicPlayerEvent
import com.example.musicplayerapp.feature.player.ui.musicplayer.MusicPlayerListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicPlayerListViewModelTest {

    private lateinit var viewModel: MusicPlayerListViewModel
    private val repository: MusicPlayerRepository = mockk()


    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MusicPlayerListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent GetMusicList - returns Success and updates state`() = runTest {
        // GIVEN
        val songList = listOf(mockSongResult, mockSongResult, mockSongResult)
        coEvery { repository.getMusicList() } returns ApiResponse.Success(songList)

        // WHEN
        viewModel.onEvent(MusicPlayerEvent.GetMusicList)
        advanceUntilIdle()

        // THEN
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(songList, state.musicList)
        assertFalse(state.isError)
    }

    @Test
    fun `onEvent GetMusicList - returns Error and updates state`() = runTest {
        // GIVEN
        coEvery { repository.getMusicList() } returns ApiResponse.Error("Data Failed")

        // WHEN
        viewModel.onEvent(MusicPlayerEvent.GetMusicList)
        advanceUntilIdle()

        // THEN
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertEquals("Data Failed", state.errorMessage)
    }

    @Test
    fun `onEvent GetMusicList - emits loading state before result`() = runTest {
        // GIVEN
        val songList = listOf(mockSongResult)
        coEvery { repository.getMusicList() } coAnswers {
            delay(10)
            ApiResponse.Success(songList)
        }

        // WHEN
        viewModel.onEvent(MusicPlayerEvent.GetMusicList)

        // THEN: Check loading state first
        assertTrue(viewModel.state.value.isLoading)

        advanceUntilIdle()

        // THEN
        val finalState = viewModel.state.value
        assertFalse(finalState.isLoading)
        assertEquals(songList, finalState.musicList)
    }

    // Mock Data
    private val mockSongResult = Song(
        album = Album(
            cover = "http://api.deezer.com/2.0/album/170781312/image",
            id = 101,
            title = "Album Title"
        ),
        artist = Artist(
            id = 202,
            name = "Artist Name"
        ),
        duration = 215,
        id = 303,
        preview = "http://www.deezer.com/track/86285553",
        title = "Song Title"
    )
}
