package com.valevich.diapro.ui.lists.adapters

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.valevich.diapro.R
import com.valevich.diapro.flows.notes.model.dto.Mood
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.tags.model.dto.Tag
import kotlinx.android.synthetic.main.view_item_note.view.*


private const val ROTATION_DEG = 180f
private const val ROTATION_START = 180f
private const val ROTATION_END = 0f

class NotesAdapter(
        private var notes: MutableList<Note> = mutableListOf(),
        private val clickAction: (note: Note) -> Unit,
        private val longClickAction: (note: Note, postition : Int) -> Boolean
) : RecyclerView.Adapter<NotesAdapter.Holder>() {

    override fun getItemCount(): Int = notes.size

    override fun onCreateViewHolder(
            parent: ViewGroup?,
            viewType: Int
    ): Holder = Holder(
            LayoutInflater.from(parent?.context).inflate(
                    R.layout.view_item_note,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(
                notes[position],
                if (position < notes.size - 1) notes[position + 1] else null,
                clickAction,
                longClickAction
        )
    }

    fun update(notes: List<Note>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        this.notes.removeAt(position)
        notifyItemRemoved(position)
    }

    @Suppress("DEPRECATION")
    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(
                note: Note,
                previousNote: Note?,
                clickAction: (note: Note) -> Unit,
                longClickAction: (note: Note, position : Int) -> Boolean
        ) {
            bindSugarLevel(note.sugarLevel)
            bindSugarChange(note.sugarLevel, previousNote?.sugarLevel ?: -1.0)
            bindMood(note.mood)
            bindInsulin(note.shortInsulin, note.longInsulin)
            bindXe(note.xe)
            bindBodyTemp(note.bodyTemperature)
            bindDate(note.date)
            bindTags(note.tags)
            bindDropDown()
            bindClickListener(clickAction, longClickAction, note)
        }

        private fun bindSugarLevel(sugarLevel: Double) {
            itemView.apply {
                if (sugarLevel > 0) {
                    sugarView.text = String.format("%.1f",sugarLevel)
                } else {
                    sugarView.visibility = INVISIBLE
                    sugarViewMetric.visibility = INVISIBLE
                    sugarChangeView.visibility = INVISIBLE
                    sugarChangeIcon.visibility = INVISIBLE
                    moodIcon.visibility = INVISIBLE
                }
            }
        }

        private fun bindSugarChange(currentSugar: Double, previousSugar: Double) {
            itemView.sugarChangeView.apply {
                if (previousSugar > 0) {
                    val difference = currentSugar - previousSugar
                    if (difference > 0) {
                        text = String.format("+%.1f", difference)
                    } else {
                        text = "$difference"
                        itemView.sugarChangeIcon.rotation = 180f
                    }
                } else {
                    visibility = INVISIBLE
                    itemView.sugarChangeIcon.visibility = INVISIBLE
                }
            }
        }

        private fun bindMood(mood: Mood) {
            itemView.moodIcon.setImageResource(mood.iconId)
        }

        private fun bindInsulin(shortInsulin: Double, longInsulin: Double) {
            itemView.units.apply {
                when {
                    shortInsulin > 0 && longInsulin > 0 -> text = String.format("%.1f/%.1f", shortInsulin, longInsulin)
                    shortInsulin > 0 -> text = String.format("%f/-", shortInsulin)
                    longInsulin > 0 -> text = String.format("%-/f", longInsulin)
                    else -> {
                        visibility = INVISIBLE
                        itemView.needle.visibility = INVISIBLE
                    }
                }
            }
        }

        private fun bindXe(xe : Double) {
            if (xe > 0) {
                itemView.foodView.text = String.format("%.1f XE", xe)
            } else {
                itemView.foodView.visibility = INVISIBLE
                itemView.foodIcon.visibility = INVISIBLE
            }
        }

        private fun bindBodyTemp(temperature : Double) {
            if (temperature > 0) {
                itemView.bodyTempView.text = Html.fromHtml(String.format("%.1f<sup>o</sup>", temperature))
            } else {
                itemView.bodyTempView.visibility = INVISIBLE
                itemView.bodyTempIcon.visibility = INVISIBLE
            }
        }

        private fun bindDate(date: String) {
            itemView.dateView.text = date
        }

        private fun bindClickListener(
                clickAction: (note: Note) -> Unit,
                longClickAction: (note: Note, position : Int) -> Boolean,
                note: Note
        ) {
            itemView.setOnClickListener { clickAction.invoke(note) }
            itemView.setOnLongClickListener { longClickAction.invoke(note, adapterPosition) }
        }

        private fun bindTags(tags: List<Tag>) {

            if (tags.isNotEmpty()) {
                bindDropDown()
                itemView.chipview.chipList = tags
            } else {
                itemView.dropDown.visibility = INVISIBLE
                itemView.divider.visibility = INVISIBLE
            }

        }

        private fun bindDropDown() {
            itemView.dropDown.apply {
                setOnClickListener {
                    val isUp = rotation == 180f
                    animate().rotationBy(ROTATION_DEG * if (isUp) -1 else 1)
                            .withEndAction {
                                rotation = if (isUp) ROTATION_END else ROTATION_START
                                itemView.chipview.visibility = if (isUp) VISIBLE else GONE
                            }
                }
            }
        }

    }
}