package com.valevich.diapro.base

import com.arellomobile.mvp.MvpPresenter
import com.valevich.diapro.base.view.BaseView
import com.valevich.diapro.rx.SchedulersSet
import com.valevich.diapro.unsafeLazy
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
            onCompleteAction: () -> Unit = {},
            scheduledTransformer: (o: Observable<T>) -> Observable<T> = { o -> o }
    ) : Disposable =
        observable.compose(applySchedulers(getScheduler(executionMode)))
                .compose(scheduledTransformer)
                .subscribe(
                        { onNextAction.invoke(it) },
                        { onErrorAction.invoke(it) },
                        { onCompleteAction.invoke() })
                .apply { addDisposable(this, executionMode) }

    internal fun <T> execute(
            executionMode: ExecutionMode,
            single: Single<T>,
            onNextAction: (T) -> Unit,
            onErrorAction: (t: Throwable) -> Unit,
            scheduledTransformer: (s: Single<T>) -> Single<T> = { s -> s }
    ): Disposable =
            single.compose(applySingleSchedulers(getScheduler(executionMode)))
                    .compose(scheduledTransformer)
                    .subscribe(
                            { onNextAction.invoke(it) },
                            { onErrorAction.invoke(it) }
                    )
                    .apply { addDisposable(this, executionMode) }

    internal fun execute(
            executionMode: ExecutionMode,
            completable: Completable,
            onCompleteAction: () -> Unit = {},
            onErrorAction: (t: Throwable) -> Unit,
            scheduledTransformer: (c: Completable) -> Completable = { c -> c }
    ): Disposable =
            completable.compose(applyCompletableSchedulers(getScheduler(executionMode)))
                    .compose { scheduledTransformer.invoke(it) }
                    .subscribe(
                            (onCompleteAction),
                            { onErrorAction.invoke(it) })
                    .apply { addDisposable(this, executionMode) }

    override fun onDestroy() {
        destroyedSubscriptions.dispose()
        super.onDestroy()
    }

    override fun detachView(view: V) {
        detachedSubscriptions.dispose()
        super.detachView(view)
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

    private fun <T> applySingleSchedulers(
            executionScheduler: Scheduler
    ): SingleTransformer<T, T> =
            SingleTransformer { o ->
                o.subscribeOn(executionScheduler)
                        .observeOn(schedulers.main())
            }

    private fun applyCompletableSchedulers(
            executionScheduler: Scheduler
    ): CompletableTransformer =
            CompletableTransformer { o ->
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