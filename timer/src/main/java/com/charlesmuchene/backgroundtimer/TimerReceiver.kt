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
        when (intent.getStringExtra(TIMER_ACTION)) {
            TIMER_INTERVAL_ACTION ->
                onTick(intent.getLongExtra(TIMER_REMAINING_TIME, -1))
            TIMER_FINISHED_ACTION -> onFinished()
        }
    }
}