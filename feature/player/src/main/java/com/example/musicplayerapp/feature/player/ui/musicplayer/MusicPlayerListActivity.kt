package com.example.musicplayerapp.feature.player.ui.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.core.coroutines.ext.observeState
import com.example.musicplayerapp.core.media.player.MusicPlayerManager
import com.example.musicplayerapp.core.ui.dialog.ProgressDialog
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.feature.player.databinding.ActivityMusicPlayerListBinding
import com.example.musicplayerapp.feature.player.service.MusicPlayerService
import com.example.musicplayerapp.feature.player.ui.musicplayer.adapter.MusicPlayerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerListActivity : AppCompatActivity() {

    @Inject
    lateinit var playerManager: MusicPlayerManager

    private lateinit var binding: ActivityMusicPlayerListBinding
    private val viewModel: MusicPlayerListViewModel by viewModels()
    private val musicPlayerAdapter by lazy {
        MusicPlayerAdapter()
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }
    private lateinit var service: MusicPlayerService
    private var isBound = false
    private var songs: List<Song> = emptyList()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val musicBinder = binder as MusicPlayerService.MusicBinder
            service = musicBinder.getService()

            if (songs.isNotEmpty()) {
                musicBinder.setMusicList(songs)
            }

            // Collect flows
            observePlayerState(musicBinder)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this@MusicPlayerListActivity, MusicPlayerService::class.java)
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)

        initView()
        initObserver()

        viewModel.onEvent(MusicPlayerEvent.GetMusicList)
    }

    private fun initView() {
        setupRecyclerView()
        onItemSelected()
        handleMusicSearch()
        initPlayerBar()
    }

    private fun setupRecyclerView() {
        binding.rvMusicList.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            adapter = musicPlayerAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    layoutManager.orientation,
                )
            )
        }
    }

    private fun initObserver() {
        this.observeState(source = viewModel.state) { state ->
            renderLoading(isLoading = state.isLoading)
            renderArticleList(musicList = state.musicList)
            handleError(isError = state.isError, errorMessage = state.errorMessage)
            renderPlayerBar(
                isPlaying = state.isPlaying,
                maxDuration = state.maxDuration,
                currentDuration = state.currentDuration,
                currentTrack = state.currentTrack,
            )
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        progressDialog.show().takeIf { isLoading } ?: progressDialog.dismiss()
    }

    private fun renderArticleList(musicList: List<Song>) {
        songs = musicList
        musicPlayerAdapter.setData(items = musicList)

        // Send to service if already bound
        updateServiceMusicListIfAvailable()
    }

    private fun initPlayerBar() {
        binding.run {
            btnPlayerRewind.setOnClickListener {
                service.binder.getMusicPlayerManager().prev()
            }
            btnPlayerPlay.setOnClickListener {
                service.binder.getMusicPlayerManager().playDefault()
            }
            btnPlayerPause.setOnClickListener {
                service.binder.getMusicPlayerManager().playPause()
            }
            btnPlayerNext.setOnClickListener {
                service.binder.getMusicPlayerManager().next()
            }
        }
    }

    private fun renderPlayerBar(
        isPlaying: Boolean,
        maxDuration: Float,
        currentDuration: Float,
        currentTrack: Song,
    ) {
        binding.run {
            playerBar.isVisible = currentTrack.id != 0
            musicTitle.text = currentTrack.title
            btnPlayerPlay.isVisible = !isPlaying
            btnPlayerPause.isVisible = isPlaying
        }
    }

    private fun updateServiceMusicListIfAvailable() {
        if (isBound && songs.isNotEmpty()) {
            service.binder.setMusicList(songs)
        }
    }

    private fun onItemSelected() {
        musicPlayerAdapter.setOnItemClickListener(object :
            MusicPlayerAdapter.OnItemClickListener {
            override fun onItemClicked(item: Song) {
                if (isBound) {
                    if (item.preview.isNotEmpty()) {
                        service.binder.getMusicPlayerManager().play(item)
                    } else {
                        Toast.makeText(
                            this@MusicPlayerListActivity,
                            "Song cannot be played",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MusicPlayerListActivity,
                        "Please Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    @OptIn(FlowPreview::class)
    private fun handleMusicSearch() {
        binding.textSearch.apply {
            callbackFlow {
                val textWatcher = doAfterTextChanged { editableText ->
                    trySend(editableText.toString())
                }

                awaitClose { removeTextChangedListener(textWatcher) }
            }.debounce(TYPING_DELAY).onEach { textInput ->
                musicPlayerAdapter.filterByArtist(query = textInput.trim())
            }.launchIn(lifecycleScope)
        }
    }

    private fun handleError(isError: Boolean, errorMessage: String) {
        if (!isError) return

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun observePlayerState(binder: MusicPlayerService.MusicBinder) {
        viewModel.onEvent(event = MusicPlayerEvent.UpdatePlayerState(binder = binder))
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
        super.onDestroy()
    }

    companion object {
        private const val TYPING_DELAY = 300L
    }
}
