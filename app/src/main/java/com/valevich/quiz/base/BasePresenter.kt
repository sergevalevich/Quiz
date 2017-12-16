package com.valevich.quiz.base

import com.arellomobile.mvp.MvpPresenter
import com.valevich.quiz.base.view.BaseView
import com.valevich.quiz.rx.SchedulersSet
import com.valevich.quiz.unsafeLazy
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<V : BaseView>(private val schedulers: SchedulersSet) : MvpPresenter<V>() {

    private val detachedSubscriptions: CompositeDisposable by unsafeLazy { CompositeDisposable() }

    private val destroyedSubscriptions: CompositeDisposable by unsafeLazy { CompositeDisposable() }

    internal fun <T> execute(
            executionMode: ExecutionMode,
            observable: Observable<T>,
            onNextAction: (T) -> Unit,
            onErrorAction: (t: Throwable) -> Unit,
            onCompleteAction: () -> Unit = {}
    ) : Disposable =
        observable.compose(applySchedulers(getScheduler(executionMode)))
                .subscribe(
                        { onNextAction.invoke(it) },
                        { onErrorAction.invoke(it) },
                        { onCompleteAction.invoke() })
                .apply { addDisposable(this, executionMode) }

    override fun onDestroy() {
        super.onDestroy()
        destroyedSubscriptions.dispose()
    }

    override fun detachView(view: V) {
        super.detachView(view)
        detachedSubscriptions.dispose()
    }

    private fun getScheduler(executionMode: ExecutionMode): Scheduler =
            when (executionMode) {
                ExecutionMode.COMPUTATION_DETACH -> schedulers.computation()
                ExecutionMode.COMPUTATION_DESTROY -> schedulers.computation()
                ExecutionMode.IO_DETACH -> schedulers.io()
                ExecutionMode.IO_DESTROY -> schedulers.io()
            }

    private fun addDisposable(
            disposable: Disposable,
            executionMode: ExecutionMode
    ) {
        when (executionMode) {
            ExecutionMode.IO_DETACH -> detachedSubscriptions.add(disposable)
            ExecutionMode.COMPUTATION_DETACH -> detachedSubscriptions.add(disposable)
            ExecutionMode.IO_DESTROY -> destroyedSubscriptions.add(disposable)
            ExecutionMode.COMPUTATION_DESTROY -> destroyedSubscriptions.add(disposable)
        }
    }


    private fun <T> applySchedulers(
            executionScheduler: Scheduler
    ): ObservableTransformer<T, T> =
            ObservableTransformer { o ->
                o.subscribeOn(executionScheduler)
                        .observeOn(schedulers.main())
            }

}

enum class ExecutionMode {
    COMPUTATION_DETACH,
    COMPUTATION_DESTROY,
    IO_DETACH,
    IO_DESTROY
}