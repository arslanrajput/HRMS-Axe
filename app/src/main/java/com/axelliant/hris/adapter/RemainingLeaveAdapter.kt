package com.axelliant.hris.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axelliant.hris.databinding.ItemLeaveBalanceBinding

import com.axelliant.hris.databinding.RemaningLeaveRowBinding
import com.axelliant.hris.model.leave.LeaveType
class RemainingLeaveAdapter(
    private val items: ArrayList<LeaveType>
) : RecyclerView.Adapter<RemainingLeaveAdapter.ViewHolder>() {

    // Cycle through these dot colors for variety
    private val dotColors = listOf(
        "#E0C5FB",  // purple  – matches app accent
        "#FFDCCE",  // coral
        "#C7DDFB",  // blue
        "#D8FFF2",  // teal
        "#FFF3DE",  // amber
        "#FFDADA",  // red
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveBalanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], dotColors[position % dotColors.size])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(
        private val binding: ItemLeaveBalanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LeaveType, dotColorHex: String) {
            binding.tvLeaveCount.text = item.value.toString() ?: "0"
            binding.tvLeaveType.text = item.title?: ""
            // Dot color
            val color = Color.parseColor(dotColorHex)
            binding.viewBg.background.mutate().setTint(color)
            binding.tvLeaveType.setTextColor(color)
            binding.tvLeaveType.setTextColor(color)

        }
    }
}