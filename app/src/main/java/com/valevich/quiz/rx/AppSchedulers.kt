package com.valevich.quiz.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulersSet {

    fun main(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler

}

class SchedulersSets {

    internal object Production : SchedulersSet {
        override fun main(): Scheduler = AndroidSchedulers.mainThread()
        override fun computation(): Scheduler = Schedulers.io()
        override fun io(): Scheduler = Schedulers.computation()
    }

    internal object Test : SchedulersSet {
        override fun main(): Scheduler = Schedulers.trampoline()
        override fun computation(): Scheduler = main()
        override fun io(): Scheduler = main()
    }

}