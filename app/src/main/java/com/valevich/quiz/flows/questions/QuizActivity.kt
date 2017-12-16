package com.valevich.quiz.flows.questions


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.valevich.quiz.R
import com.valevich.quiz.appComponent
import com.valevich.quiz.base.view.BaseActivity
import com.valevich.quiz.common.Extras.QUIZ_CATEGORY_EXTRA
import com.valevich.quiz.common.Extras.QUIZ_QUESTIONS_EXTRA
import com.valevich.quiz.flows.categories.builder.QuizActivityModule
import com.valevich.quiz.flows.categories.entity.QuizCategory
import com.valevich.quiz.flows.questions.entity.Question
import com.valevich.quiz.flows.questions.presenter.QuizPresenter
import com.valevich.quiz.flows.questions.view.QuizView
import com.valevich.quiz.flows.results.ResultsActivity
import com.valevich.quiz.unsafeLazy
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import io.github.kbiakov.codeview.highlight.Font
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : BaseActivity(), QuizView {

    @InjectPresenter
    lateinit var presenter: QuizPresenter

    override fun showResults(questions: List<Question>) {
        Intent(this, ResultsActivity::class.java).apply {
            putParcelableArrayListExtra(QUIZ_QUESTIONS_EXTRA, ArrayList(questions))
            startActivity(this)
        }
        finish()
    }

    override fun animate() {
        presenter.onQuestionDisplayed()
    }

    override fun showWrongOption(answer: Int) {
        getOption(answer).apply {
            background = getDrawable(R.drawable.option_bg_wrong)
        }
    }

    override fun updateTimer(progressValue: Int) {
        quizTimer.progress = progressValue
    }

    override fun showRightAnswer(rightAnswerPosition: Int) {
        getOption(rightAnswerPosition).background = getDrawable(R.drawable.option_bg_right)
    }

    override fun showProgress() {
        quizProgressBar.visibility = VISIBLE
        quizContainer.visibility = GONE
    }

    override fun hideProgress() {
        quizProgressBar.visibility = GONE
        quizContainer.visibility = VISIBLE
    }

    override fun hideQuestion() {
        quizHeader.apply {
            YoYo.with(Techniques.SlideOutUp)
                    .duration(800)
                    .onEnd {
                        questionText.apply {
                            text = ""
                            questionText.visibility = GONE
                        }
                        questionSubText.apply {
                            text = ""
                            visibility = GONE
                        }
                        questionSnippet.apply {
                            visibility = GONE
                        }
                        firstOption.apply {
                            background = getDrawable(R.drawable.option_bg)
                        }
                        secondOption.apply {
                            background = getDrawable(R.drawable.option_bg)
                        }
                        thirdOption.apply {
                            background = getDrawable(R.drawable.option_bg)
                        }
                        fourthOption.apply {
                            background = getDrawable(R.drawable.option_bg)
                        }
                        quizTimer.progress = 0
                        presenter.loadNextQuestion()
                    }
                    .playOn(this)
        }
    }

    override fun showTitle(title: String) {
        YoYo.with(Techniques.SlideInDown).duration(800).playOn(quizHeader)
        questionText.apply {
            text = title
            visibility = VISIBLE
        }
    }

    override fun showSubTitle(subTitle: String) {
        questionSubText.apply {
            text = subTitle
            visibility = VISIBLE
        }
    }

    override fun showCodeSnippet(code: String) {
        YoYo.with(Techniques.SlideInDown).duration(800).playOn(quizHeader)
        questionSnippet.apply {
            questionSnippet.setOptions(
                    Options.Default.get(this@QuizActivity)
                            .withLanguage("java")
                            .withCode(Html.fromHtml(code).toString())
                            .withTheme(ColorTheme.MONOKAI)
                            .withFont(Font.DejaVuSansMono)
            )
            visibility = VISIBLE
        }
    }

    override fun showOptions(answers: List<String>) {
        answers.forEachIndexed { index, answer ->
            getOption(index).apply {
                YoYo.with(Techniques.ZoomIn).duration(800).playOn(this)
                text = answer
                setOnClickListener { presenter.answer(index) }
            }
        }
    }

    override fun disableOptions() {
        firstOption.setOnClickListener(null)
        secondOption.setOnClickListener(null)
        thirdOption.setOnClickListener(null)
        fourthOption.setOnClickListener(null)
    }

    private val component by unsafeLazy {
        appComponent()
                .quizActivityComponentBuilder()
                .quizActivityModule(QuizActivityModule(extractCategoryExtra()))
                .build()
    }

    private fun extractCategoryExtra(): QuizCategory =
            intent.getParcelableExtra(QUIZ_CATEGORY_EXTRA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
    }

    @ProvidePresenter
    fun provideQuizPresenter(): QuizPresenter = component.presenter()

    private fun getOption(position: Int): TextView = when (position) {
        0 -> firstOption
        1 -> secondOption
        2 -> thirdOption
        3 -> fourthOption
        else -> firstOption
    }

}