package com.valevich.diapro.flows.notes.view.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatEditText
import android.view.View.GONE
import android.view.View.VISIBLE
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.plumillonforge.android.chipview.Chip
import com.valevich.diapro.R
import com.valevich.diapro.appComponent
import com.valevich.diapro.base.view.BaseActivity
import com.valevich.diapro.common.DEFAULT_VALUE
import com.valevich.diapro.common.Extras
import com.valevich.diapro.common.Extras.NOTE_EXTRA
import com.valevich.diapro.common.Extras.TAGS_EXTRA
import com.valevich.diapro.common.REQUEST_CODE_TAGS
import com.valevich.diapro.common.TEMP_DEFAULT
import com.valevich.diapro.flows.navigation.NavigationActivity
import com.valevich.diapro.flows.notes.model.dto.Mood
import com.valevich.diapro.flows.notes.model.dto.Note
import com.valevich.diapro.flows.notes.presenter.add.NewNotePresenter
import com.valevich.diapro.flows.tags.model.dto.Tag
import com.valevich.diapro.flows.tags.view.TagsActivity
import com.valevich.diapro.showSnackBar
import com.valevich.diapro.ui.lists.adapters.MoodPickerAdapter
import com.valevich.diapro.unsafeLazy
import com.valevich.diapro.util.toStringFrom
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.android.synthetic.main.fragment_notes.*
import java.util.*
import kotlin.collections.ArrayList


class NewNoteActivity : BaseActivity(), NewNoteView {

    private val component by unsafeLazy {
        appComponent().newNoteComponentBuilder().build()
    }

    @InjectPresenter
    internal lateinit var newNotePresenter: NewNotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar()
        setSaveButtonListener()
        setupMoodPicker()
        setUpTagsPicker()
        val note: Note? = intent.getParcelableExtra(NOTE_EXTRA)
        note?.let {
            showNote(it)
        }
    }

    override fun disableSaveButton() {
        saveButton.setOnClickListener(null)
    }

    override fun enableSaveButton() {
        setSaveButtonListener()
    }

    override fun close() {
        startActivity(Intent(this,NavigationActivity::class.java))
        finish()
    }

    override fun showProgress() {
        addNoteContent.visibility = GONE
        newNoteProgressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        addNoteContent.visibility = VISIBLE
        newNoteProgressBar.visibility = GONE
    }

    override fun showError(message: String) {
        showSnackBar(
                message = message,
                rootView = rootNew,
                length = Snackbar.LENGTH_SHORT
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,NavigationActivity::class.java))
    }

    override fun layoutId(): Int = R.layout.activity_new_note

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TAGS && resultCode == Activity.RESULT_OK) {
            val tags: List<Tag> = data?.getParcelableArrayListExtra(Extras.TAGS_EXTRA) ?: listOf()
            newNoteChipview.chipList = tags
            newNoteChipview.visibility = VISIBLE
        }
    }

    @ProvidePresenter
    internal fun provideNewNotePresenter(): NewNotePresenter = component.newNotePresenter()

    private fun showNote(note: Note) {
        sugarLevelLabel.setText(note.sugarLevel.toString())
        tempLabel.setText(note.bodyTemperature.toString())
        note.longInsulin.let {
            if (it != DEFAULT_VALUE) {
                longInsulinLabel.setText(it.toString())
            }
        }
        note.shortInsulin.let {
            if (it != DEFAULT_VALUE) {
                shortInsulinLabel.setText(it.toString())
            }
        }
        note.xe.let {
            if (it != DEFAULT_VALUE) {
                xeLabel.setText(it.toString())
            }
        }
        moodPicker.setSelection(Mood.values().binarySearch(note.mood))
        note.tags.let {
            if (it.isNotEmpty()) {
                newNoteChipview.chipList = it
                newNoteChipview.visibility = VISIBLE
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(newNoteToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.new_item)
    }

    private fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            newNotePresenter.onSaveClicked(createNote())
        }
    }

    private fun setupMoodPicker() {
        val adapter = MoodPickerAdapter(this, R.layout.spinner_item_foreground, Mood.values().toList())
        adapter.setDropDownViewResource(R.layout.spinner_item)
        moodPicker.adapter = adapter
    }

    private fun setUpTagsPicker() {
        tagsPicker.setOnClickListener {
            val chips: MutableList<Chip> = newNoteChipview.chipList
            val tags: ArrayList<Tag> = ArrayList(chips as ArrayList<Tag>)
            startActivityForResult(Intent(this, TagsActivity::class.java).putParcelableArrayListExtra(TAGS_EXTRA, tags), REQUEST_CODE_TAGS)
        }
    }

    private fun getNoteExtra() : Note? = intent.getParcelableExtra(NOTE_EXTRA)

    private fun createNote(): Note = Note(
            id = getNoteExtra()?.id ?: -1,
            sugarLevel = getDoubleFrom(sugarLevelLabel),
            date = toStringFrom(Date()),
            bodyTemperature = getDoubleFrom(tempLabel, TEMP_DEFAULT),
            longInsulin = getDoubleFrom(longInsulinLabel),
            shortInsulin = getDoubleFrom(shortInsulinLabel),
            xe = getDoubleFrom(xeLabel),
            mood = getMood(),
            tags = getTags()
    )

    private fun getDoubleFrom(editText: AppCompatEditText, def: Double = DEFAULT_VALUE): Double = try {
        editText.text.toString().toDouble()
    } catch (e: Exception) {
        def
    }

    private fun getMood(): Mood = moodPicker.selectedItem as Mood

    private fun getTags(): List<Tag> = (newNoteChipview?.chipList ?: listOf()) as? List<Tag> ?: listOf()

}