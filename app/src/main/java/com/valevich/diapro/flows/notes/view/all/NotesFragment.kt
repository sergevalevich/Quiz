package com.valevich.diapro.flows.notes.view.all

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.valevich.diapro.R
import com.valevich.diapro.appComponent
import com.valevich.diapro.base.view.BaseFragment
import com.valevich.diapro.common.Extras.NOTE_EXTRA
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.notes.presenter.all.NotesPresenter
import com.valevich.diapro.flows.notes.view.add.NewNoteActivity
import com.valevich.diapro.showSnackBar
import com.valevich.diapro.ui.lists.adapters.NotesAdapter
import com.valevich.diapro.unsafeLazy
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : BaseFragment(), NotesView, ActionMode.Callback {

    @InjectPresenter
    lateinit var notesPresenter: NotesPresenter

    private val component by unsafeLazy {
        appComponent().notesComponentBuilder().build()
    }

    private val notesAdapter by unsafeLazy {
        NotesAdapter(
                clickAction = clickAction,
                longClickAction = longClickAction
        )
    }

    private var actionMode: ActionMode? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            notesPresenter.onCreateNoteButtonClick()
        }
        notesList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = notesAdapter
        }
    }

    override fun reset() {
        hideProgress()
    }

    override fun showNotes(notes: List<Note>) {
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        notesList.layoutAnimation = controller
        notesAdapter.update(notes)
        notesList.scheduleLayoutAnimation()
    }

    override fun openEditNoteScreen(note: Note) {
        startActivity(Intent(activity, NewNoteActivity::class.java).putExtra(NOTE_EXTRA, note))
        activity.finish()
    }

    override fun openNewNoteScreen() {
        startActivity(Intent(activity, NewNoteActivity::class.java))
        activity.finish()
    }

    override fun removeListItem(position: Int) {
        notesAdapter.remove(position)
    }

    override fun showProgress() {
        progressBar.visibility = VISIBLE
        notesList.visibility = GONE
        fab.visibility = GONE
    }

    override fun hideProgress() {
        progressBar.visibility = GONE
        notesList.visibility = VISIBLE
        fab.visibility = VISIBLE
    }

    override fun showError(message: String) {
        showSnackBar(
                message = message,
                rootView = notesRoot,
                actionText = getString(R.string.retry),
                action = { notesPresenter.loadNotes() }
        )
    }

    override fun layoutId(): Int = R.layout.fragment_notes

    private fun startActionMode() {
        actionMode = (activity as AppCompatActivity).startSupportActionMode(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.contextual_action_bar, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        //notesPresenter.releaseSelectedItem()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_remove -> {
                actionMode?.finish()
                notesPresenter.removeSelectedItem()
                return true
            }
        }
        return false
    }

    @ProvidePresenter
    internal fun providePresenter() = component.notesPresenter()

    private val clickAction:(note: Note) -> Unit  = { notesPresenter.onNoteItemClick(it) }

    private val longClickAction:(note: Note, position :Int) -> Boolean  = {
        note, pos ->
        if (actionMode == null) {
            startActionMode()
            notesPresenter.onNoteItemLongClick(note, pos)
        }
        true
    }

}