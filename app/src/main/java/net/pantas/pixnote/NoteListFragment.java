package net.pantas.pixnote;

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

	private void updateUI() {
		NoteManager noteManager = Container.instance(getActivity()).getNoteManager();
		ArrayList<Note> notes = noteManager.list();

		mNoteAdapter = new NoteAdapter(notes);
		mRecyclerView.setAdapter(mNoteAdapter);
	}

	class NoteViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.list_item_note_title)
		TextView mTitleTextView;

		@BindView(R.id.list_item_note_date)
		TextView mDateTextView;

		@BindView(R.id.list_item_note_active)
		CheckBox mActiveCheckBox;

		NoteViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);
		}

		void bindNote(Note note) {
			mTitleTextView.setText(note.getTitle());
			mDateTextView.setText(note.getFormattedDate());
			mActiveCheckBox.setChecked(note.isActive());
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
