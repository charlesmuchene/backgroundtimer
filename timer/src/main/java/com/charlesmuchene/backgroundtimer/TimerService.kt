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

package com.charlesmuchene.backgroundtimer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.START_TIMER
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.STOP_TIMER_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_DURATION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_FINISHED_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_INTERVAL
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_INTERVAL_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_REMAINING_TIME
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_TAG

/**
 * Timer Service
 */
internal class TimerService : Service() {

    private var isRunning = false
    private var tag: String? = null
    private lateinit var timer: SenseiTimer
    private lateinit var stopReceiver: BroadcastReceiver
    private lateinit var broadcastManager: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()
        Log.e("Tag", "We are creating service")
        stopReceiver = StopReceiver()
        broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager.registerReceiver(stopReceiver, IntentFilter(STOP_TIMER_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Tag", "We are finishing service")
        broadcastManager.unregisterReceiver(stopReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        evaluateAction(intent)
        return Service.START_STICKY
    }

    /**
     * Evaluate timer action
     *
     * @param intent [Intent]
     */
    private fun evaluateAction(intent: Intent?) {
        intent ?: return
        val action = intent.getStringExtra(TIMER_ACTION) ?: return
        val timerTag = intent.getStringExtra(TIMER_TAG) ?: return
        if (!isRunning && action == START_TIMER) {
            tag = timerTag
            isRunning = true
            startTimer(timerTag, intent)
        }
    }

    private fun startTimer(tag: String, intent: Intent) {
        val duration = intent.getLongExtra(TIMER_DURATION, 1_000)
        val interval = intent.getLongExtra(TIMER_INTERVAL, 1_000)
        timer = SenseiTimer(duration, interval, tag, ::broadcastTimerInterval, ::broadcastTimerFinished)
        timer.start()
    }

    private fun broadcastTimerInterval(tag: String, remaining: Long) {
        broadcastManager.sendBroadcast(Intent(TIMER_ACTION).apply {
            putExtra(TIMER_ACTION, TIMER_INTERVAL_ACTION)
            putExtra(TIMER_REMAINING_TIME, remaining)
            putExtra(TIMER_TAG, tag)
        })
    }

    private fun broadcastTimerFinished(tag: String) {
        val intent = Intent(TIMER_ACTION).apply {
            putExtra(TIMER_ACTION, TIMER_FINISHED_ACTION)
            putExtra(TIMER_TAG, tag)
        }
        broadcastManager.sendBroadcast(intent)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * Stop action receiver
     */
    private inner class StopReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            if (isRunning && intent.getStringExtra(TIMER_TAG) == tag) {
                isRunning = false
                timer.cancel()
                stopSelf()
            }
        }

    }
}