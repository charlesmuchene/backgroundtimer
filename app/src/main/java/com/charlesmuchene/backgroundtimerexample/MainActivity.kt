/* Copyright (C) 2018 Charles Muchene
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.charlesmuchene.backgroundtimerexample

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.charlesmuchene.backgroundtimer.BackgroundTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var backgroundTimer: BackgroundTimer
    private lateinit var anotherTimer: BackgroundTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backgroundTimer = BackgroundTimer(this, 3_000, 500, ::onTick, ::onFinished)
        anotherTimer = BackgroundTimer(this, 5_000, 1_000, {
            Log.d("AnotherTimer", "Timing: $it")
        }) { Log.d("AnotherTimer", "Finished Another timer") }

        startTimerButton.setOnClickListener { startTimer() }
        stopTimerButton.setOnClickListener { stopTimer() }
    }

    private fun startTimer() {
        stopTimerButton.isEnabled = true
        startTimerButton.isEnabled = false
        backgroundTimer.start()
    }

    private fun stopTimer() {
        stopTimerButton.isEnabled = false
        startTimerButton.isEnabled = true
        backgroundTimer.stop()
        timerTextView.text = getString(R.string.timer_stopped)
    }

    override fun onStart() {
        super.onStart()
        backgroundTimer.start()
        anotherTimer.start()
    }

    override fun onStop() {
        super.onStop()
        backgroundTimer.stop()
        anotherTimer.stop()
    }

    @SuppressLint("SetTextI18n")
    private fun onTick(remaining: Long) {
        timerTextView.text = "$remaining ms"
        Log.d("TestTimer", "The remaining time is $remaining")
    }

    private fun onFinished() {
        stopTimerButton.isEnabled = false
        startTimerButton.isEnabled = true
        timerTextView.text = getString(R.string.timer_finished)
        Log.d("TestTimer", "We have finished")
    }
}
