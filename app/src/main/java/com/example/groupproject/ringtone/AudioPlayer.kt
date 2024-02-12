package com.example.groupproject.ringtone

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.groupproject.R
import java.io.IOException

class AudioPlayer: AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ringtone)

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer?.setDataSource(allRingtones().allMyRingtones[1].url) // put my array of links here
            mediaPlayer?.setOnPreparedListener {

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // setting up a click listener for my button
        val playButton: Button = findViewById(R.id.ringtone_playButton)
        playButton.setOnClickListener {
            println("Button Pressed")
            // check if the media player is not null and not playing
            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
                // Start playback when the button is clicked
                mediaPlayer?.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}