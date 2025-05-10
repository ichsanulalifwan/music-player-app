package com.example.musicplayerapp.feature.player.ui.musicplayer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.core.coroutines.ext.observeState
import com.example.musicplayerapp.core.ui.dialog.ProgressDialog
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.feature.player.databinding.ActivityMusicPlayerListBinding
import com.example.musicplayerapp.feature.player.ui.musicplayer.adapter.MusicPlayerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MusicPlayerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerListBinding
    private val viewModel: MusicPlayerListViewModel by viewModels()
    private val musicPlayerAdapter by lazy {
        MusicPlayerAdapter()
    }
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserver()

        viewModel.onEvent(MusicPlayerEvent.GetMusicList)
    }

    private fun initView() {
        setupRecyclerView()
        onItemSelected()
        handleMusicSearch()
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
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        progressDialog.show().takeIf { isLoading } ?: progressDialog.dismiss()
    }

    private fun renderArticleList(musicList: List<Song>) {
        musicPlayerAdapter.setData(items = musicList)
    }

    private fun onItemSelected() {
        musicPlayerAdapter.setOnItemClickListener(object :
            MusicPlayerAdapter.OnItemClickListener {
            override fun onItemClicked(item: Song) {

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

    companion object {
        private const val TYPING_DELAY = 300L
    }
}
