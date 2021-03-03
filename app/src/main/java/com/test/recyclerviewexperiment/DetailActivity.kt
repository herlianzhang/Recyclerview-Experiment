package com.test.recyclerviewexperiment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.test.recyclerviewexperiment.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(IMAGE_URL_KEY)
        binding.ivMain.transitionName = url
        Glide.with(this).load(url).into(binding.ivMain)
    }
}