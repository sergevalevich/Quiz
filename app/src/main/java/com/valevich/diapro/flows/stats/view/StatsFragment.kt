package com.valevich.diapro.flows.stats.view

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.valevich.diapro.R
import com.valevich.diapro.appComponent
import com.valevich.diapro.base.view.BaseFragment
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.stats.presenter.StatsPresenter
import com.valevich.diapro.flows.tags.model.dto.Tag
import com.valevich.diapro.showSnackBar
import com.valevich.diapro.ui.custom.ChartMarkerView
import com.valevich.diapro.unsafeLazy
import com.valevich.diapro.util.toMillisFrom
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_stats.*


class StatsFragment : BaseFragment(), StatsView, AdapterView.OnItemSelectedListener {

    private val component by unsafeLazy {
        appComponent().statsComponentBuilder().build()
    }

    private val adapter: ArrayAdapter<Tag>  by unsafeLazy {
        ArrayAdapter<Tag>(activity,
                android.R.layout.simple_spinner_item,
                mutableListOf()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tagsSpinner.adapter = this
        }
    }

    @InjectPresenter
    internal lateinit var presenter: StatsPresenter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tagsSpinner.onItemSelectedListener = this
    }

    override fun reset() {
        hideProgress()
    }

    override fun showProgress() {
        statsProgressBar.visibility = View.VISIBLE
        chart.visibility = View.GONE
        tagsSpinner.visibility = View.GONE
    }

    override fun hideProgress() {
        statsProgressBar.visibility = View.GONE
        chart.visibility = View.VISIBLE
        tagsSpinner.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        showSnackBar(
                message = message,
                rootView = notesRoot,
                actionText = getString(R.string.retry),
                action = { presenter.loadStats() }
        )
    }

    override fun showStatsByTag(notes: List<Note>, tag: Tag?) {
        if (tag == null) {
            setUpChart(notes)
        } else {
            setUpChart(notes, tag.name)
        }
    }

    override fun showTags(tags: List<Tag>) {
        adapter.clear()
        adapter.addAll(tags)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.loadStats(parent?.getItemAtPosition(position) as? Tag)
    }

    override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture) {

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            chart?.highlightValues(null) // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    override fun layoutId(): Int = R.layout.fragment_stats

    @ProvidePresenter
    internal fun providePresenter() = component.statsPresenter()

    private fun setUpChart(notes: List<Note>, title: String = getString(R.string.order_default)) {
        chart.apply {
            onChartGestureListener = this@StatsFragment
            setDrawGridBackground(false)
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            val markerView = ChartMarkerView(activity, R.layout.custom_marker_view)
            markerView.chartView = this
            marker = markerView
            xAxis.enableGridDashedLine(0.1f, 0.1f, 0f)
            //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());

            axisLeft.apply {
                removeAllLimitLines() // reset all limit lines to avoid overlapping lines
                axisMaximum = 40f
                axisMinimum = 0f
                enableGridDashedLine(0.1f, 0.1f, 0f)
                setDrawZeroLine(false)
                setDrawLimitLinesBehindData(true)
            }
            axisRight.isEnabled = false
        }

        setData(notes, title)


        chart.animateX(1000)


        val l = chart.legend

        // modify the legend ...
        l.form = LegendForm.LINE

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }

    private fun setData(notes: List<Note>, title: String) {

        val values = ArrayList<Entry>()

        notes.forEach {
            values.add(Entry(
                    toMillisFrom(it.date).toFloat(),
                    it.sugarLevel.toFloat(),
                    resources.getDrawable(R.drawable.add)
            ))
        }


        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, title)

            set1.setDrawIcons(false)

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            val drawable = ContextCompat.getDrawable(activity, R.drawable.fade_red)
            set1.fillDrawable = drawable

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            chart?.data = data
        }
    }
}