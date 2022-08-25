package com.example.workapp.GameFragment.recyclerView

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workapp.R
import com.example.workapp.databinding.ItemBinding

class GameAdapter :
    RecyclerView.Adapter<GameViewHolder>() {

    var itemClickListener: ((position: Int, value: Boolean) -> Unit)? = null
    var items = mutableListOf<CircleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context,
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int = items.size

}

class GameViewHolder(private val binding: ItemBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    var itemClickListener: ((position: Int, value: Boolean) -> Unit)? = null

    fun bind(item: CircleItem) {
        when {
            !item.active && item.animate -> {
                binding.circleView.animate()
                    .setDuration(100)
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .start()
            }
            item.active && item.animate -> {
                binding.circleView.animate()
                    .setDuration(0)
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .start()

                binding.circleView.animate()
                    .setDuration(100)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .start()
            }
            else -> {
                binding.circleView.animate()
                    .setDuration(0)
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .start()
            }
        }

        binding.circleView.setOnClickListener {
            itemClickListener?.invoke(adapterPosition, item.active)
        }
    }
}



