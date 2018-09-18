package com.charlesmuchene.backgroundtimer

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import java.util.*

/**
 * Background timer class.
 *
 * NB: Do not use this for precise timing requirements. The timer
 * has delays that might be significant due to interval and finish broadcasts.
 *
 * @param context [Context] Android context in scope
 * @param duration Timer duration
 * @param interval Timer interval
 * @param onTick Code block to execute on every tick
 * @param onFinished Code block to execute on timer finish
 */
class BackgroundTimer(private val context: Context, private val duration: Long,
                      private val interval: Long, onTick: (Long) -> Unit,
                      private val onFinished: () -> Unit) {

    private val tag = UUID.randomUUID().toString()
    private val receiver: TimerReceiver
    private var isRunning = false

    init {
        receiver = TimerReceiver(context, tag, onTick, ::onTimerFinished)
    }

    /**
     * Start timer
     */
    fun start() {
        if (isRunning) return
        isRunning = true

        val intent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_TAG, tag)
            putExtra(TIMER_ACTION, START_TIMER)
            putExtra(TIMER_DURATION, duration)
            putExtra(TIMER_INTERVAL, interval)
        }
        context.startService(intent)
        receiver.register()
    }

    /**
     * Stop timer
     */
    fun stop() {
        val intent = Intent(STOP_TIMER_ACTION).apply {
            putExtra(TIMER_TAG, tag)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        receiver.unregister()
        isRunning = false
    }

    private fun onTimerFinished() {
        receiver.unregister()
        isRunning = false
        onFinished()
    }

    companion object {
        internal const val TIMER_FINISHED_ACTION = "timer_finished_action"
        internal const val TIMER_INTERVAL_ACTION = "timer_interval_action"
        internal const val TIMER_REMAINING_TIME = "timer_remaining_time"
        internal const val STOP_TIMER_ACTION = "stop_timer_action"
        internal const val TIMER_DURATION = "timer_duration"
        internal const val TIMER_INTERVAL = "timer_interval"
        internal const val TIMER_ACTION = "timer_action"
        internal const val START_TIMER = "start_timer"
        internal const val TIMER_TAG = "timer_tag"

        internal val TIMER_ACTION_INTENT_FILTER = IntentFilter(TIMER_ACTION)

    }
}