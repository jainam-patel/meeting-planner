package com.android_project.meetingplanner.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.android_project.meetingplanner.R
import com.android_project.meetingplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_main)
        mainBinding.tv.startAnimation(alphaAnimation)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@MainActivity, DateActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 5000)


    }
}