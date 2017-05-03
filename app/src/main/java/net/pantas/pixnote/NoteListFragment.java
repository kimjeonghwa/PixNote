package net.pantas.pixnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteListFragment extends Fragment {
	private static final int REQUEST_NOTE = 1;

	@BindView(R.id.notes_recycler_view)
	RecyclerView mRecyclerView;
	NoteAdapter mNoteAdapter;
	private UUID mUpdatedNoteId = null;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note_list, container, false);
		ButterKnife.bind(NoteListFragment.this, view);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		updateUI();

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_NOTE) {
			mUpdatedNoteId = NoteFragment.getChangedUUID(resultCode, data);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		updateUI();
	}

	private void updateUI() {
		NoteManager noteManager = Container.instance(getActivity()).getNoteManager();
		ArrayList<Note> notes = noteManager.list();

		if (mNoteAdapter == null) {
			mNoteAdapter = new NoteAdapter(notes);
			mRecyclerView.setAdapter(mNoteAdapter);
		}
		else if (mUpdatedNoteId != null) {
			mNoteAdapter.notifyNoteChanged(mUpdatedNoteId);
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

			mUpdatedNoteId = null;
			Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
			startActivityForResult(intent, REQUEST_NOTE);
		}
	}

	private class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
		private final List<Note> mNotes;

		NoteAdapter(List<Note> notes) {
			mNotes = notes;
		}

		void notifyNoteChanged(UUID id) {
			for (int i = 0; i < mNotes.size(); i++) {
				if (mNotes.get(i).getId() == id) {
					notifyItemChanged(i);
					return;
				}
			}

			// We don't have this id, better reload all, just in case
			notifyDataSetChanged();
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
