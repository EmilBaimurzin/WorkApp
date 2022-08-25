package com.example.workapp.mainFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.workapp.R
import com.example.workapp.ViewBindingFragment
import com.example.workapp.databinding.MainFragmentBinding

class FragmentMain: ViewBindingFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {
    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_gameFragment)
        }

        binding.rulesButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setView(R.layout.rules)
                .create()
                .show()
        }
        val score = sharedPreferences.getInt("Score", 0)
        binding.scoreTextView.text = "Highest score is $score"
    }
}