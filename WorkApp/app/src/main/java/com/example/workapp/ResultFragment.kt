package com.example.workapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.workapp.databinding.ResultFragmentBinding

class ResultFragment: ViewBindingFragment<ResultFragmentBinding>(ResultFragmentBinding::inflate) {
    private val _score: ResultFragmentArgs by navArgs()
    private var score = 0
    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("Prefs", MODE_PRIVATE)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        score = _score.score
        binding.scoreTextView.text = "Your score is $score!"
        requireActivity().onBackPressedDispatcher.addCallback(this) {}

        if (sharedPreferences.getInt("Score", 0) < score) {
            sharedPreferences.edit().putInt("Score", score).apply()
        }

        binding.retryButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
        }
        binding.mainMenuButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_fragmentMain)
        }
    }
}