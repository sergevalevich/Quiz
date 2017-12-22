package com.valevich.diapro.flows.tags.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.valevich.diapro.R
import com.valevich.diapro.appComponent
import com.valevich.diapro.base.view.BaseActivity
import com.valevich.diapro.common.Extras
import com.valevich.diapro.flows.tags.builder.TagsModule
import com.valevich.diapro.flows.tags.model.dto.Tag
import com.valevich.diapro.flows.tags.presenter.TagsPresenter
import com.valevich.diapro.showSnackBar
import com.valevich.diapro.ui.lists.adapters.TagsAdapter
import com.valevich.diapro.unsafeLazy
import kotlinx.android.synthetic.main.activity_tags.*
import kotlinx.android.synthetic.main.fragment_notes.*

class TagsActivity : BaseActivity(), TagsView, ActionMode.Callback  {

    @InjectPresenter
    internal lateinit var tagsPresenter : TagsPresenter

    private val component by unsafeLazy {
        appComponent()
                .tagsComponentBuilder()
                .tagsModule(TagsModule(getSelectedTags()))
                .build()
    }

    private val tagsAdapter by unsafeLazy { TagsAdapter(
            clickAction = clickAction,
            longClickAction = longClickAction
    ) }

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar()
        setUpTagsList()
        setUpFab()
    }

    override fun showAllTags(tags: List<Pair<Tag,Boolean>>) {
        tagsAdapter.update(tags)
    }

    override fun reset() {
        hideProgress()
    }

    override fun showProgress() {
        tagsList.visibility = GONE
        progressViewTags.visibility = VISIBLE
    }

    override fun hideProgress() {
        tagsList.visibility = VISIBLE
        progressViewTags.visibility = GONE
    }

    override fun showError(message: String) {
        showSnackBar(
                message = message,
                rootView = notesRoot,
                actionText = getString(R.string.retry),
                action = { tagsPresenter.loadTags() }
        )
    }

    override fun removeListItem(position: Int) {
        tagsAdapter.remove(position)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent().putParcelableArrayListExtra(
                Extras.TAGS_EXTRA,
                ArrayList(tagsAdapter.items().filter { it.second }.map { it.first })
        ))
        super.onBackPressed()
    }

    override fun layoutId(): Int  = R.layout.activity_tags

    @ProvidePresenter
    internal fun providePresenter() : TagsPresenter {
        return component.tagsPresenter()
    }

    private fun setUpActionBar() {
        setSupportActionBar(tagsToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.tags_screen_title)
    }

    private fun setUpTagsList() {
        tagsList.apply {
            layoutManager = LinearLayoutManager(this@TagsActivity)
            adapter = tagsAdapter
        }
    }

    private fun setUpFab() {
        tagsFab.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(tag : Tag = Tag()) {
        MaterialDialog.Builder(this)
                .title(title)
                .theme(Theme.LIGHT)
                .widgetColorRes(R.color.colorAccent)
                .contentColorRes(R.color.textColorPrimary)
                .titleColorRes(R.color.textColorPrimary)
                .inputRangeRes(1, 15, R.color.colorAccent)
                .positiveText(android.R.string.ok)
                .input("", tag.name, {
                    _, input -> tagsPresenter.saveTag(tag.copy(name = input.toString()))
                })
                .build()
                .show()
    }

    private fun getSelectedTags() : List<Tag> = intent.getParcelableArrayListExtra(Extras.TAGS_EXTRA)

    private fun startActionMode() {
        actionMode = startSupportActionMode(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.contextual_action_bar, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_remove -> {
                actionMode?.finish()
                tagsPresenter.removeSelectedItem()
                return true
            }
        }
        return false
    }

    private val clickAction:(tag: Tag) -> Unit  = { showDialog(it) }

    private val longClickAction:(tag: Tag, pos : Int) -> Boolean  = {
        tag, pos ->
        if (actionMode == null) {
            startActionMode()
            tagsPresenter.onNoteItemLongClick(tag, pos)
        }
        true
    }
}