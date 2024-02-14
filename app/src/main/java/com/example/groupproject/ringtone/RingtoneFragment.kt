package com.example.groupproject.ringtone

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRingtoneBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.ringtoneList.adapter = RingtoneListAdapter(RingtoneListAdapter.OnClickListener {
//            viewModel.ringtone_Properties.value

            initializeMediaPlayer(it.muisicFile)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initializeMediaPlayer(@RawRes musicFile :Int) {
//        mediaPlayer?.release() // Release any existing Media player Instance
//
//        try {
//            mediaPlayer = MediaPlayer.create(requireContext(), musicFile)
//            mediaPlayer?.start()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        var mediaPlayer = MediaPlayer.create(requireContext(), musicFile)
        mediaPlayer.start()
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }
//
//        val playButton: Button = view.findViewById(R.id.ringtoneButton)
//        playButton.setOnClickListener {
//            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
//                mediaPlayer?.start()
//            }
//        }
//    private fun initializeMediaPlayer(musicFile: Int) { original
//        mediaPlayer = MediaPlayer()
//        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
//        println(viewModel.selectedRingtone.value?.name.toString()) // selected ringtone is my property
//        println(musicFile)
////        playAudio(mContext = context., musicFile)
//
//        try {
//            mediaPlayer.setDataSource(musicFile.toString()) // has to do with audio not view
//            mediaPlayer.prepareAsync()
//            mediaPlayer.setOnPreparedListener {
//                // Media player is prepared, but don't start playback here
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

//    private fun stopAudio() { // original
//        try {
//            mediaPlayer.release()
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

//    fun playAudio(mContext: Context, fileName: Int) { // original
//        try {
//            stopAudio()
//            mediaPlayer = MediaPlayer.create(mContext, mContext.resources.getIdentifier(fileName.toString(), "raw", mContext.packageName))
//            mediaPlayer.start()
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

//    override fun onDestroy() { // original
//        super.onDestroy()
//        mediaPlayer.release()
//    }
}