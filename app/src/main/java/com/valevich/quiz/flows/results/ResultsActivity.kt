package com.valevich.quiz.flows.results

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.valevich.quiz.R
import com.valevich.quiz.base.view.BaseActivity
import com.valevich.quiz.common.Extras.QUIZ_QUESTIONS_EXTRA
import com.valevich.quiz.flows.categories.view.QuizCategoriesActivity
import com.valevich.quiz.ui.lists.adapters.ResultsAdapter
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        setSupportActionBar(resultsToolbar)
        setTitle(R.string.results_title)

        questionsListView.layoutManager = LinearLayoutManager(this)
        questionsListView.adapter = ResultsAdapter(intent.getParcelableArrayListExtra(QUIZ_QUESTIONS_EXTRA))

        continueButton.setOnClickListener {
            startActivity(Intent(this, QuizCategoriesActivity::class.java))
        }
    }

}