package com.valevich.diapro

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.valevich.diapro.flows.notes.model.all.NotesModel
import com.valevich.diapro.flows.notes.presenter.all.NotesPresenter
import com.valevich.diapro.flows.notes.view.all.NotesView
import com.valevich.diapro.rx.SchedulersSet
import com.valevich.diapro.rx.SchedulersSets
import io.reactivex.Observable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class NotesPresenterSpek : Spek({

    val model: NotesModel by memoized {
        mock<NotesModel>()
    }

    val view: NotesView by memoized {
        mock<NotesView>()
    }

    val shedulers: SchedulersSet by memoized {
        SchedulersSets.Test
    }

    given("notes presenter") {

        val presenter: NotesPresenter by memoized {
            NotesPresenter(
                    model, shedulers
            )
        }

        on("loading notes successfully") {

            whenever(model.getNotes()).thenReturn(Observable.just(listOf()))

            presenter.attachView(view)

            it("should show views") {

                verify(view).showProgress()
                verify(view).showNotes(any())
                verify(view).hideProgress()

            }

        }
    }


})