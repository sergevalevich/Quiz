package com.valevich.diapro.ui.lists.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.valevich.diapro.R
import com.valevich.diapro.flows.notes.model.dto.Mood


class MoodPickerAdapter(
        ctx: Context,
        resource: Int,
        private val moods: List<Mood>
) : ArrayAdapter<Mood>(ctx, resource, moods) {

    override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_foreground, parent, false)
            holder = ViewHolder()
            holder.icon = view?.findViewById(R.id.moodSpinner)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.icon?.setImageResource(moods[position].iconId)

        return view!!
    }

    private class ViewHolder(
            var icon: ImageView? = null
    )
}