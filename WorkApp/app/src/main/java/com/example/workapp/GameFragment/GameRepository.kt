package com.example.workapp.GameFragment

import android.os.CountDownTimer
import android.util.Log
import com.example.workapp.GameFragment.recyclerView.CircleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GameRepository {
    fun generateBooleanList(): Flow<MutableList<CircleItem>> {
        return flow {
            while (true) {
                val listToReturn = getEmptyList().toMutableList()
                listToReturn[(0..8).random()].apply {
                    active = true
                    animate = true
                }
                emit(listToReturn)
                delay((700..1500).random().toLong())
            }
        }.flowOn(Dispatchers.Default)
    }

    fun getEmptyList(): MutableList<CircleItem> {
        return mutableListOf(
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
            CircleItem(active = false, animate = false),
        )
    }
}