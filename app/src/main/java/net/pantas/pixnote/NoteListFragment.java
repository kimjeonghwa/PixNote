package net.pantas.pixnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class NoteListFragment extends Fragment {
	@BindView(R.id.notes_recycler_view)
	RecyclerView mRecyclerView;
	NoteAdapter mNoteAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note_list, container, false);
		ButterKnife.bind(NoteListFragment.this, view);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		updateUI();

		return view;
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
		} else {
			mNoteAdapter.notifyDataSetChanged();
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

			Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
			startActivity(intent);
		}
	}

	private class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
		private final List<Note> mNotes;

		NoteAdapter(List<Note> notes) {
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
