package com.valevich.diapro.ui.custom

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.valevich.diapro.R

class ChartMarkerView(
        context : Context,
        layoutResource : Int
) : MarkerView(context, layoutResource) {

    private val tvContent : TextView = findViewById(R.id.tvContent)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent.apply {
            text = if (e is CandleEntry) {
                "" + Utils.formatNumber(e.high, 0, true)
            } else {
                "" + Utils.formatNumber(e!!.y, 0, true)
            }
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF = MPPointF(-(width / 2.0).toFloat(), -height.toFloat())
}