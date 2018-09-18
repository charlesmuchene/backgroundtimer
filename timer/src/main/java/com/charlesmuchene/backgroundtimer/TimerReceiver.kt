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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_ACTION_INTENT_FILTER
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_FINISHED_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_INTERVAL_ACTION
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_REMAINING_TIME
import com.charlesmuchene.backgroundtimer.BackgroundTimer.Companion.TIMER_TAG

/**
 * Timer receiver
 *
 * @param context [Context]
 * @param tag Timer tag to listen to
 * @param onTick On tick block
 * @param onFinished On timer finished block
 */
internal class TimerReceiver(context: Context,
                             private val tag: String, private val onTick: (Long) -> Unit,
                             private val onFinished: () -> Unit) : BroadcastReceiver() {

    private val broadcastManager = LocalBroadcastManager.getInstance(context)

    /**
     * Register this receiver
     */
    fun register() {
        broadcastManager.registerReceiver(this, TIMER_ACTION_INTENT_FILTER)
    }

    /**
     * Unregister this receiver
     */
    fun unregister() {
        broadcastManager.unregisterReceiver(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action != TIMER_ACTION) return
        if (intent.getStringExtra(TIMER_TAG) != tag) return

        when (intent.getStringExtra(TIMER_ACTION)) {
            TIMER_INTERVAL_ACTION ->
                onTick(intent.getLongExtra(TIMER_REMAINING_TIME, -1))
            TIMER_FINISHED_ACTION -> onFinished()
        }
    }
}