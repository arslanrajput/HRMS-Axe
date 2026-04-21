package com.yourapp.ui.leaves.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axelliant.hris.databinding.ItemUpcomingLeaveBinding
import com.axelliant.hris.model.leave.UpcomingLeaves
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Horizontal-scroll adapter for upcoming leave cards.
 * Each card shows leave type, formatted date range, duration, and status badge.
 */
class UpcomingLeaveAdapter(
    private val items: ArrayList<UpcomingLeaves>
) : RecyclerView.Adapter<UpcomingLeaveAdapter.ViewHolder>() {

    private val serverFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayFmt = SimpleDateFormat("MMM d", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingLeaveBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(
        private val binding: ItemUpcomingLeaveBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UpcomingLeaves) {

            // ✅ Leave type
            binding.tvLeaveTypeTag.text = item.leave_type ?: "Leave"

            // ✅ Dates
            val startLabel = formatDate(item.from_date)
            val endLabel = formatDate(item.to_date)
            binding.tvLeaveDates.text =
                if (startLabel == endLabel) startLabel else "$startLabel – $endLabel"

            // ✅ Duration
            val days = item.total_leave_days ?: 1.0
            binding.tvLeaveDuration.text =
                if (days == 1.0) "1 day" else "${days.toInt()} days"

            // ✅ Status
            val status = item.status ?: "Pending"
            binding.tvLeaveStatus.text = status
/*
            // ✅ Backend color (PRIORITY)
            val backendColor = item.meta.color

            if (!backendColor.isNullOrEmpty()) {
                applyDynamicColor(backendColor)
            } else {
            }*/
        }

        private fun formatDate(raw: String?): String {
            if (raw.isNullOrBlank()) return "-"
            return runCatching {
                displayFmt.format(serverFmt.parse(raw)!!)
            }.getOrDefault(raw)
        }

        // ✅ NEW: Dynamic color from backend
        private fun applyDynamicColor(hex: String) {
            runCatching {
                val color = Color.parseColor(hex)

                // 🔥 LEFT ACCENT (make sure viewAccent exists in XML)
                binding.viewAccent.setBackgroundColor(color)

                // 🔥 STATUS BADGE
                binding.tvLeaveStatus.apply {
                    background.mutate().setTint(adjustAlpha(color, 0.15f))
                    setTextColor(color)
                }

                // 🔥 OPTIONAL: Leave type color
                binding.tvLeaveTypeTag.setTextColor(color)

            }.onFailure {
                applyStatusStyle(binding.tvLeaveStatus.text.toString())
            }
        }

        // ✅ Fallback (existing)
        private fun applyStatusStyle(status: String) {
            val (bgColor, textColor) = when (status.lowercase()) {
                "approved"  -> Pair("#E1F5EE", "#0F6E56")
                "rejected"  -> Pair("#FCEBEB", "#A32D2D")
                "pending"   -> Pair("#FAEEDA", "#854F0B")
                "cancelled" -> Pair("#F1EFE8", "#5F5E5A")
                else        -> Pair("#EEEDFE", "#3C3489")
            }

            binding.tvLeaveStatus.apply {
                background.mutate().setTint(Color.parseColor(bgColor))
                setTextColor(Color.parseColor(textColor))
            }

            // Optional: reset accent if no backend color
            binding.viewAccent.setBackgroundColor(Color.TRANSPARENT)
        }

        // ✅ Helper for soft background
        private fun adjustAlpha(color: Int, factor: Float): Int {
            val alpha = (Color.alpha(color) * factor).toInt()
            return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
        }
    }
}