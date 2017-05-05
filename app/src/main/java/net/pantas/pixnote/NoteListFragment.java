package net.pantas.pixnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment {
	private static final int REQUEST_NOTE = 1;
	private static final String SAVED_SUBTITLE_VISIBLE = "SAVED_SUBTITLE_VISIBLE";

	@BindView(R.id.notes_recycler_view)
	RecyclerView mRecyclerView;
	NoteAdapter mNoteAdapter;
	private boolean mSubtitleVisible;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note_list, container, false);
		ButterKnife.bind(NoteListFragment.this, view);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		if (savedInstanceState != null) {
			mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
		}

		updateUI();

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();

		updateUI();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.fragment_note_list, menu);

		MenuItem item = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible) {
			item.setTitle(R.string.menu_hide_subtitle);
		} else {
			item.setTitle(R.string.menu_show_subtitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_note:
				Note note = new Note();
				Container.instance(getActivity()).getNoteManager().add(note);
				startActivity(NotePagerActivity.newIntent(getActivity(), note.getId()));
				return true;

			case R.id.menu_item_show_subtitle:
				mSubtitleVisible = !mSubtitleVisible;
				getActivity().invalidateOptionsMenu();
				updateSubtitle();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void updateUI() {
		NoteManager noteManager = Container.instance(getActivity()).getNoteManager();
		ArrayList<Note> notes = noteManager.list();

		if (mNoteAdapter == null) {
			mNoteAdapter = new NoteAdapter(notes);
			mRecyclerView.setAdapter(mNoteAdapter);
		} else {
			mNoteAdapter.setNotes(notes);
			mNoteAdapter.notifyDataSetChanged();
		}

		updateSubtitle();
	}

	private void updateSubtitle() {
		NoteManager manager = Container.instance(getActivity()).getNoteManager();

		String subtitle;
		if (mSubtitleVisible) {
			subtitle = getString(R.string.menu_subtitle_format, manager.size());
		} else {
			subtitle = null;
		}

		ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (ab != null) {
			ab.setSubtitle(subtitle);
		}
	}

	class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.list_item_note_title)
		TextView mTitleTextView;

		@BindView(R.id.list_item_note_date)
		TextView mDateTextView;

		@BindView(R.id.list_item_note_active)
		CheckBox mActiveCheckBox;

		private Note mNote;

		NoteViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);

			itemView.setOnClickListener(this);
		}

		void bindNote(Note note) {
			mNote = note;
			mTitleTextView.setText(note.getTitle());
			mDateTextView.setText(note.getFormattedDate());
			mActiveCheckBox.setChecked(note.isActive());
		}

		@Override
		public void onClick(View v) {
			if (mNote == null) {
				return;
			}

			Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());
			startActivityForResult(intent, REQUEST_NOTE);
		}
	}

	private class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
		private List<Note> mNotes;

		NoteAdapter(List<Note> notes) {
			mNotes = notes;
		}

		public void setNotes(List<Note> notes) {
			mNotes = notes;
		}

		@Override
		public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.list_item_note, parent, false);
			return new NoteViewHolder(view);
		}

		@Override
		public void onBindViewHolder(NoteViewHolder holder, int position) {
			Note note = mNotes.get(position);
			holder.bindNote(note);
		}

		@Override
		public int getItemCount() {
			return mNotes.size();
		}
	}
}
