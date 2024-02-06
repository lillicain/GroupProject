package com.example.groupproject.ringtone

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.R
import com.example.groupproject.databinding.FragmentRingtoneBinding
import java.io.IOException

class RingtoneFragment: Fragment() {

    private val viewModel: RingtoneViewModel by lazy {
        ViewModelProvider(this)[RingtoneViewModel::class.java]
    }

    private var mediaPlayer: MediaPlayer? = null

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // initialize MediaPlayer and set up the button on click listener
//
//        initializeMediaPlayer()
//
//        val playButton: Button = view.findViewById(R.id.ringtone_playButton)
//        playButton.setOnClickListener {
//            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
//                mediaPlayer?.start()
//            }
//        }
//    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val binding = FragmentRingtoneBinding.inflate(inflater)
//    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        println(viewModel.selectedRingtone.value?.name.toString()) // selected ringtone is my property

        try {
            mediaPlayer?.setDataSource("")
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                // Media player is prepared, but don't start playback here
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}