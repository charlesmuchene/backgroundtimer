package com.charlesmuchene.backgroundtimerexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.charlesmuchene.backgroundtimer.BackgroundTimer

class MainActivity : AppCompatActivity() {

    private lateinit var backgroundTimer: BackgroundTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backgroundTimer = BackgroundTimer(this, 3_000, 500, ::onTick, ::onFinished)
    }

    override fun onStart() {
        super.onStart()
        backgroundTimer.start()
    }

    override fun onStop() {
        super.onStop()
        backgroundTimer.stop()
    }

    private fun onTick(remaining: Long) {
        Log.d("TestTimer", "The remaining time is $remaining")
    }

    private fun onFinished() {
        Log.d("TestTimer", "We have finished")
    }
}
