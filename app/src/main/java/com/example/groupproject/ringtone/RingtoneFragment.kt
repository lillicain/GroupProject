package com.example.groupproject.ringtone

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.R
import com.example.groupproject.databinding.FragmentRingtoneBinding
import java.io.IOException
import java.util.zip.Inflater

class RingtoneFragment: Fragment() {
    private val viewModel: RingtoneViewModel by lazy {
        ViewModelProvider(this)[RingtoneViewModel::class.java]
    }

    private var mediaPlayer: MediaPlayer? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRingtoneBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.ringtoneList.adapter = RingtoneListAdapter(RingtoneListAdapter.OnClickListener {
            initializeMediaPlayer(it.muisicFile)
            // up to date
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initializeMediaPlayer(@RawRes musicFile :Int) {

        var mediaPlayer = MediaPlayer.create(requireContext(), musicFile) // required context is used on anything post androidx while context is used before androidx.
        mediaPlayer.start()
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        viewModel.selectedRingtone(
//            when (item.itemId) {
//                R.id.
//            }
//        )
//    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }
}