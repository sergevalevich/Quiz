package com.valevich.quiz.flows.categories.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.firebase.ui.auth.AuthUI
import com.valevich.quiz.*
import com.valevich.quiz.base.view.BaseActivity
import com.valevich.quiz.common.Extras
import com.valevich.quiz.firebase.MainActivity
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.categories.presenter.QuizCategoriesPresenter
import com.valevich.quiz.flows.questions.QuizActivity
import com.valevich.quiz.ui.lists.GridSpacingItemDecoration
import com.valevich.quiz.ui.lists.adapters.QuizCategoriesAdapter
import kotlinx.android.synthetic.main.activity_categories.*

class QuizCategoriesActivity : BaseActivity(), QuizCategoriesView {

    @InjectPresenter
    lateinit var presenter: QuizCategoriesPresenter

    private val component by unsafeLazy {
        appComponent().categoriesActivityComponentBuilder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setSupportActionBar(toolbar)
        setTitle(R.string.categories_title)
        categoriesListView.apply {
            val spanCount = 2
            layoutManager = GridLayoutManager(this@QuizCategoriesActivity, spanCount)
            addItemDecoration(GridSpacingItemDecoration(spanCount, dpToPixels(2f).toInt(), includeEdge = true))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.categories_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.logout) {
            logout()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }

    override fun showCategories(quizCategories: List<QuizCategory>) {
        categoriesListView.adapter = QuizCategoriesAdapter(quizCategories) { category ->
            goToQuiz(category)
        }
    }

    override fun goToQuiz(category: QuizCategory) {
        Intent(this, QuizActivity::class.java).apply {
            putExtra(Extras.QUIZ_CATEGORY_EXTRA, category)
            startActivity(this)
        }
    }

    override fun showProgress() {
        progressBar.visibility = VISIBLE
        categoriesListView.visibility = GONE
    }

    override fun hideProgress() {
        progressBar.visibility = GONE
        categoriesListView.visibility = VISIBLE
    }

    override fun showError(message: String) {
        showSnackBar(
                message = message,
                rootView = root,
                actionText = "Retry",
                action = { presenter.retry() }
        )
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener({
                    startActivity(Intent(this, MainActivity::class.java))
                })
    }

    @ProvidePresenter
    fun provideCategoriesPresenter(): QuizCategoriesPresenter = component.presenter()

}
