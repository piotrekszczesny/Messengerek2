package com.example.messengerek

import android.media.MediaPlayer
import android.media.session.MediaController
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.messengerek.HighlitsActivity.Companion.HIGH_TITLE
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_single_highlit.*

class SingleHighlitActivity : AppCompatActivity() {

    companion object {

        val TAG = "Wiadomości"
        var currentUser: User? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_highlit)
        val title = intent.getStringExtra(HighlitsActivity.HIGH_TITLE)



        supportActionBar?.title = title



        fetchSingleHigh()
    }


    private fun fetchSingleHigh(){
    val video = intent.getStringExtra(HighlitsActivity.VIDEO_URL)

        videoView.setVideoPath(video)
        var mediaController:android.widget.MediaController = android.widget.MediaController(this)
        videoView.setMediaController(mediaController)
        videoView.start()






    }

}
