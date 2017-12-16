package com.valevich.quiz.flows.questions.presenter

import com.arellomobile.mvp.InjectViewState
import com.valevich.quiz.base.BasePresenter
import com.valevich.quiz.base.ExecutionMode
import com.valevich.quiz.flows.questions.entity.Question
import com.valevich.quiz.flows.questions.model.QuizModel
import com.valevich.quiz.flows.questions.view.QuizView
import com.valevich.quiz.rx.SchedulersSet
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@InjectViewState
class QuizPresenter @Inject constructor(
        schedulersSet: SchedulersSet,
        private val quizModel: QuizModel
) : BasePresenter<QuizView>(schedulersSet) {

    private var timerDisposable: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadNextQuestion()
    }

    fun onQuestionDisplayed() {
        startTimer()
    }

    fun answer(answer: Int) {
        viewState.disableOptions()
        execute(
                ExecutionMode.COMPUTATION_DETACH,
                quizModel.acceptAnswer(answer),
                { answeredQuestion ->
                    if (!answeredQuestion.hasAnsweredCorrect) {
                        viewState.showWrongOption(answer)
                    }
                },
                { viewState.showError() },
                {
                    viewState.showRightAnswer(quizModel.currentQuestion().correctAnswerPosition)
                    viewState.hideQuestion()
                }
        )
    }

    fun loadNextQuestion() {
        execute(
                ExecutionMode.COMPUTATION_DESTROY,
                quizModel.getNextQuestion(),
                { question ->
                    if (question.title == "terminate") {
                        showSummary()
                    } else {
                        showQuestion(question)
                    }
                },
                { viewState.showError() }
        )
    }

    private fun showQuestion(question: Question) {
        viewState.apply {
            question.apply {
                showOptions(answers)
                when {
                    codeSnippet != null && codeSnippet.isNotEmpty() -> {
                        showCodeSnippet(codeSnippet)
                        showSubTitle(title)
                    }
                    else -> showTitle(title)
                }
                animate()
            }
        }
    }

    private fun showSummary() {
        viewState.showProgress()
        execute(
                ExecutionMode.COMPUTATION_DETACH,
                quizModel.getAnsweredQuestions(),
                { questions -> viewState.showResults(questions) },
                {
                    viewState.hideProgress()
                    viewState.showError()
                },
                {
                    viewState.hideProgress()
                }
        )
    }

    private fun startTimer() {
        timerDisposable?.dispose()
        timerDisposable = execute(
                ExecutionMode.COMPUTATION_DESTROY,
                quizModel.countDown(),
                { tick -> viewState.updateTimer(tick.toInt()) },
                { viewState.showError() },
                {
                    viewState.disableOptions()
                    viewState.showRightAnswer(quizModel.currentQuestion().correctAnswerPosition)
                    viewState.hideQuestion()
                }
        )
    }

}