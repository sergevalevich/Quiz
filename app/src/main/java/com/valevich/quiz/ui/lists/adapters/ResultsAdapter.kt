package com.valevich.quiz.ui.lists.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.valevich.quiz.R
import com.valevich.quiz.flows.questions.entity.Question
import kotlinx.android.synthetic.main.view_item_question.view.*

class ResultsAdapter(
        private val questions: List<Question>
) : RecyclerView.Adapter<ResultsAdapter.Holder>() {

    override fun getItemCount(): Int = questions.size

    override fun onCreateViewHolder(
            parent: ViewGroup?,
            viewType: Int
    ): Holder = Holder(
            LayoutInflater.from(parent?.context).inflate(
                    R.layout.view_item_question,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.itemView?.apply {
            questions[position].apply {
                titleView.text = title
                questionNumber.text = (position + 1).toString()
                image.setImageResource(
                        if (hasAnsweredCorrect) {
                            R.drawable.ic_right
                        } else {
                            R.drawable.ic_wrong
                        }
                )
            }
        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}