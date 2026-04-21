package com.axelliant.hris.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.axelliant.hris.R
import com.axelliant.hris.callback.AdapterItemClick
import com.axelliant.hris.databinding.ModulesRowBinding
import com.axelliant.hris.model.Modules

class ModulesAdapter(
    private val list: List<Modules>,
    private val itemClick: AdapterItemClick
) : RecyclerView.Adapter<ModulesAdapter.ModuleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ModulesRowBinding.inflate(layoutInflater, parent, false)
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(list[position])

        holder.binding.lyModule.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                // Add click animation
                animateItemClick(holder.itemView) {
                    itemClick.onItemClick(list[adapterPosition], adapterPosition)
                }
            }
        }
    }

    private fun animateItemClick(view: View, onAnimationEnd: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f)
        val scaleXBack = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f)
        val scaleYBack = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f)

        scaleX.duration = 100
        scaleY.duration = 100
        scaleXBack.duration = 100
        scaleYBack.duration = 100

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleY)
        animatorSet.play(scaleXBack).after(scaleX)
        animatorSet.play(scaleYBack).after(scaleY)

        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                onAnimationEnd()
            }
        })

        animatorSet.start()
    }

    override fun getItemCount(): Int = list.size

    class ModuleViewHolder(
        val binding: ModulesRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Modules) {
            // Set text content
            binding.tvModule.text = item.name
            binding.tvDescription.text = item.description
            binding.tvDescription.visibility = View.VISIBLE

            // Set icon
            binding.ivLoc.setImageDrawable(item.drawable)

            // Set icon background tint
            binding.ivLoc.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, item.iconBgColor)
            )

            // Set gradient background to card content
            item.color?.let { gradientDrawable ->
                binding.cardContent.background = gradientDrawable
            }
        }
    }
}