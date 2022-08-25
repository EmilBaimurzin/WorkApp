package com.example.workapp.GameFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.workapp.GameFragment.recyclerView.GameAdapter
import com.example.workapp.R
import com.example.workapp.ViewBindingFragment
import com.example.workapp.databinding.GameFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class GameFragment : ViewBindingFragment<GameFragmentBinding>(GameFragmentBinding::inflate) {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var gameAdapter: GameAdapter
    private var generatorJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        binding.scoreTextView.text = viewModel.getScoresState().toString()
        if (viewModel.getPauseState()) {
            showPause()
        } else {
            start()
        }
        binding.pauseButton.setOnClickListener {
            showPause()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_gameFragment_to_fragmentMain)
        }
    }

    private fun launch() {
        generatorJob = lifecycleScope.launch {
            viewModel.generateBooleanList()
                .collect { list ->
                    gameAdapter.items = list
                    list.forEachIndexed { index, circleItem ->
                        if (circleItem.active) timerForItem(index)
                    }
                    gameAdapter.notifyDataSetChanged()
                }
            delay(500)
        }
    }

    private fun initList() {
        gameAdapter = GameAdapter()
        with(binding.gameRecyclerView) {
            adapter = gameAdapter
            layoutManager = object : GridLayoutManager(requireContext(), 3) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
        gameAdapter.itemClickListener = { position, value ->
            onItemClick(position, value)
        }
        binding.gameRecyclerView.itemAnimator = null
    }

    private fun onItemClick(position: Int, value: Boolean) {
        if (value) {
            gameAdapter.items[position].active = false
            gameAdapter.notifyItemChanged(position)
            binding.scoreTextView.text =
                (binding.scoreTextView.text.toString().toInt() + 1).toString()
            viewModel.setScoresState(binding.scoreTextView.text.toString().toInt())
        }
    }

    private fun timerForItem(position: Int) {
        lifecycleScope.launch {
            delay(600)
            gameAdapter.items[position].let {
                it.active = false
                gameAdapter.notifyItemChanged(position)
                delay(50)
                it.animate = false
            }
        }
    }

    private fun showPause() {
        viewModel.setPauseState(true)
        generatorJob?.cancel()
        viewModel.stopTimer()
        AlertDialog.Builder(requireContext())
            .setTitle("Pause")
            .setNeutralButton("Quit") { dialog, _ ->
                dialog.cancel()
                findNavController().navigate(R.id.action_gameFragment_to_fragmentMain)
            }
            .setPositiveButton("Continue") { dialog, _ ->
                binding.startTextView.text = ""
                startTimer()
                dialog.cancel()
                launch()
            }
            .setOnCancelListener {
                viewModel.setPauseState(false)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun startTimer() {
        viewModel.startTimer({
            onTick(it)
        }, {
            onFinish()
        })
    }

    private fun onTick(time: Long) {
        viewModel.setMillisecondsState(time)
        val timeToSeconds = TimeUnit.MILLISECONDS.toSeconds(time)
        val formatTime = String.format("%02d", timeToSeconds)
        binding.timerTextView.text = formatTime
    }

    private fun onFinish() {
        generatorJob?.cancel()
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToResultFragment(
            viewModel.getScoresState()))
        binding.timerTextView.text = "00"
    }

    private fun start() {
        generatorJob = lifecycleScope.launch {
            gameAdapter.items = viewModel.getEmptyList()
            binding.startTextView.text = "3"
            delay(1000)
            binding.startTextView.text = "2"
            delay(1000)
            binding.startTextView.text = "1"
            delay(1000)
            binding.startTextView.text = "Start!"
            launch()
            startTimer()
            delay(500)
            binding.startTextView.text = ""
        }
    }

    override fun onPause() {
        super.onPause()
        generatorJob?.cancel()
        viewModel.stopTimer()
    }
}

