package com.example.lab12music

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.lab12music.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())

    private val tracks = listOf(
        Track("Жарко", "Чёрная метка", "Loqiemean ft. Куок", R.raw.track1, R.drawable.cover1),
        Track("Лавина V2", "У себя на кухне", "Loqiemean", R.raw.track2, R.drawable.cover2),
        Track("Лопасти", "Контроль", "Loqiemean", R.raw.track3, R.drawable.cover3),
        Track("Потом", "Пов3стка", "Loqiemean", R.raw.track4, R.drawable.cover4),
        Track("Как у Людей", "У себя на кухне", "Loqiemean", R.raw.track5, R.drawable.cover2)
    )

    private var currentTrackIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.studentInfo.text = getString(R.string.student_info)

        setupTrack(currentTrackIndex)
        setupListeners()
    }

    private fun setupTrack(index: Int) {
        val track = tracks[index]
        binding.trackTitle.text = track.title
        binding.albumName.text = track.album
        binding.artistName.text = track.artist
        binding.albumCover.setImageResource(track.cover)

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, track.fileRes)
        binding.seekBar.max = mediaPlayer?.duration ?: 0
        binding.totalTime.text = formatTime(mediaPlayer?.duration ?: 0)
        updateSeekBar()
    }

    private fun setupListeners() {
        binding.playPauseButton.setOnClickListener {
            if (isPlaying) pauseTrack() else playTrack()
        }

        binding.nextButton.setOnClickListener {
            if (currentTrackIndex < tracks.size - 1) {
                currentTrackIndex++
                setupTrack(currentTrackIndex)
                playTrack()
            }
        }

        binding.previousButton.setOnClickListener {
            if (currentTrackIndex > 0) {
                currentTrackIndex--
                setupTrack(currentTrackIndex)
                playTrack()
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playTrack() {
        mediaPlayer?.start()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        updateSeekBar()
    }

    private fun pauseTrack() {
        mediaPlayer?.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.ic_play)
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { player ->
            binding.seekBar.progress = player.currentPosition
            binding.currentTime.text = formatTime(player.currentPosition)
            if (isPlaying) {
                handler.postDelayed({ updateSeekBar() }, 1000)
            }
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(null)
    }
}
