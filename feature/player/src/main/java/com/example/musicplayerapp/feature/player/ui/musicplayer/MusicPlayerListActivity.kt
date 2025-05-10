package com.example.musicplayerapp.feature.player.ui.musicplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerapp.core.coroutines.ext.observeState
import com.example.musicplayerapp.feature.player.databinding.ActivityMusicPlayerListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlayerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerListBinding
    private val viewModel: MusicPlayerListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        enableEdgeToEdge()
        initView()
        initObserver()
    }

    private fun initView() {
//        binding?.rvJobList?.also { view ->
//            view.adapter = jobAdapter
//            view.itemAnimator = null
//            view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        }
//        handleSearch()
        viewModel.onEvent(MusicPlayerEvent.GetMusicList)
    }

    private fun initObserver() {
        this.observeState(source = viewModel.state) {
            // TODO: Handle state
        }
    }
}