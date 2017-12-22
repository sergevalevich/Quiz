package com.valevich.diapro.ui.lists.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.valevich.diapro.R
import com.valevich.diapro.flows.tags.model.dto.Tag
import kotlinx.android.synthetic.main.view_item_tag.view.*

class TagsAdapter(
        private var tags: MutableList<Pair<Tag, Boolean>> = mutableListOf(),
        private val clickAction: (tag: Tag) -> Unit,
        private val longClickAction: (tag: Tag, pos : Int) -> Boolean
) : RecyclerView.Adapter<TagsAdapter.Holder>() {

    override fun getItemCount(): Int = tags.size

    override fun onCreateViewHolder(
            parent: ViewGroup?,
            viewType: Int
    ): Holder = Holder(
            LayoutInflater.from(parent?.context).inflate(
                    R.layout.view_item_tag,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(tags[position])
    }

    fun items() : MutableList<Pair<Tag,Boolean>> = tags

    @Suppress("DEPRECATION")
    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(
                tagToState: Pair<Tag, Boolean>
        ) {
            itemView.tagNameLabel.text = tagToState.first.name
            itemView.tagCheckBox.isChecked = tagToState.second
            itemView.tagCheckBox.setOnCheckedChangeListener {
                _, isChecked ->
                tags.removeAt(adapterPosition)
                tags.add(tagToState.copy(second = isChecked))
            }
            itemView.setOnClickListener { clickAction.invoke(tagToState.first) }
            itemView.setOnLongClickListener { longClickAction.invoke(tagToState.first, adapterPosition) }
        }

    }

    fun update(tags: List<Pair<Tag, Boolean>>) {
        this.tags.clear()
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        tags.removeAt(position)
        notifyItemRemoved(position)
    }
}