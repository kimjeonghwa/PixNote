package net.pantas.pixnote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoteListFragment extends Fragment {
	@BindView(R.id.notes_recycler_view) RecyclerView mRecyclerView;
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

	private class NoteViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitleTextView;

		public NoteViewHolder(View itemView) {
			super(itemView);

			mTitleTextView = (TextView) itemView;
		}
	}

	private class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
		private final List<Note> mNotes;

		public NoteAdapter(List<Note> notes) {
			mNotes = notes;
		}

		@Override
		public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			return new NoteViewHolder(view);
		}

		@Override
		public void onBindViewHolder(NoteViewHolder holder, int position) {
			Note note = mNotes.get(position);
			holder.mTitleTextView.setText(note.getTitle());
		}

		@Override
		public int getItemCount() {
			return mNotes.size();
		}
	}
}
