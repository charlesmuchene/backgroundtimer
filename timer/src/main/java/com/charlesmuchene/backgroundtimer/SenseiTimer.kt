package com.charlesmuchene.backgroundtimer

import android.os.CountDownTimer

/**
 * Sensei timer class
 *
 * @param duration Duration of the timer
 * @param interval Interval of the timer
 * @param onInterval Code block to execute on every interval
 * @param onEnd Code block to execute on timer finish
 */
class SenseiTimer(duration: Long, interval: Long, private val tag: String,
                  private val onInterval: (String, Long) -> Unit,
                  private val onEnd: (String) -> Unit) : CountDownTimer(duration, interval) {

    override fun onFinish() {
        onEnd(tag)
    }

    override fun onTick(millisUntilFinished: Long) {
        onInterval(tag, millisUntilFinished)
    }

}
