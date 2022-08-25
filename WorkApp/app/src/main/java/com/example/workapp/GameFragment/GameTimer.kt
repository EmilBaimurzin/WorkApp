package com.example.workapp.GameFragment

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit

class GameTimer(
    private val millisecond: Long,
    private val callback: (Long) -> Unit,
    private val onFinish: () -> Unit
) : CountDownTimer(millisecond, 1000) {

    override fun onTick(p0: Long) {
        callback(p0)
    }

    override fun onFinish() = onFinish.invoke()
}
