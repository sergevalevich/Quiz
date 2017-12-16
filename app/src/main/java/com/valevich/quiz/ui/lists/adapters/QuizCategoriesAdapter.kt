package com.valevich.quiz.ui.lists.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.valevich.quiz.R
import com.valevich.quiz.flows.categories.entity.QuizCategory
import kotlinx.android.synthetic.main.view_item_category.view.*

class QuizCategoriesAdapter(
        private val categories: List<QuizCategory>,
        private val clickAction: (category: QuizCategory) -> Unit
) : RecyclerView.Adapter<QuizCategoriesAdapter.CategoriesHolder>() {

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(
            parent: ViewGroup?,
            viewType: Int
    ): CategoriesHolder = CategoriesHolder(
            LayoutInflater.from(parent?.context).inflate(
                    R.layout.view_item_category,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: CategoriesHolder?, position: Int) {
        holder?.itemView?.apply {
            categories[holder.adapterPosition].apply {
                setOnClickListener { clickAction.invoke(this)}
                title.text = name
                progress.text = String.format("%d/%d", answered, questions.size)

                Glide.with(context)
                        .load(imageUrl)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .transition(withCrossFade())
                        .into(icon)
            }
        }
    }

    class CategoriesHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}