package com.valevich.diapro.flows.stats.view

import android.view.MotionEvent
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.valevich.diapro.base.view.BaseView
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag

interface StatsView : BaseView, OnChartGestureListener {

    fun showTags(tags: List<Tag>)

    fun showStatsByTag(notes: List<Note>, tag: Tag? = null)

    override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture) {

    }
    override fun onChartLongPressed(me: MotionEvent) {
    }

    override fun onChartDoubleTapped(me: MotionEvent) {
    }

    override fun onChartSingleTapped(me: MotionEvent) {
    }

    override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {
    }

    override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {
    }

    override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
    }
}
