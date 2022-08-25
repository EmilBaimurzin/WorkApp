package com.example.workapp.GameFragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.workapp.GameFragment.recyclerView.CircleItem
import kotlinx.coroutines.flow.Flow

class GameViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val state = savedState
    private val repository = GameRepository()
    private lateinit var timer: GameTimer

    fun getEmptyList(): MutableList<CircleItem> {
        return repository.getEmptyList()
    }

    fun generateBooleanList(): Flow<MutableList<CircleItem>> {
        return repository.generateBooleanList()
    }

    fun startTimer(onTick: (Long) -> Unit, onFinish: () -> Unit) {
        timer = GameTimer(getMillisecondsState(), {onTick(it)}, {onFinish.invoke()})
        timer.start()
    }

    fun stopTimer() {
        try {
            timer.cancel()
        } catch (t: Throwable) {

        }
    }

    fun setMillisecondsState(millisecond: Long) {
        state[MILLIS_KEY] = millisecond
    }

    private fun getMillisecondsState(): Long = state[MILLIS_KEY] ?: 30000

    fun setScoresState(score: Int) {
        state[SCORE_KEY] = score
    }

    fun getScoresState(): Int = state[SCORE_KEY] ?: 0

    fun setPauseState(value: Boolean) {
        state[PAUSE_KEY] = value
    }

    fun getPauseState(): Boolean = state[PAUSE_KEY] ?: false


    companion object {
        private const val MILLIS_KEY = "MILLIS_KEY"
        private const val SCORE_KEY = "SCORE_KEY"
        private const val PAUSE_KEY = "PAUSE_KEY"
    }

}