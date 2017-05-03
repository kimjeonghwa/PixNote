package net.pantas.pixnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;

import java.util.UUID;

public class NoteFragment extends Fragment {
	private static final String ARG_NOTE_ID = "ARG_NOTE_ID";
	private static final String EXTRA_NOTE_ID = "net.pantas.pixnote.EXTRA_NOTE_ID";

	private Note mNote;

	@BindView(R.id.note_title_edit) protected EditText mTitleEdit;
	@BindView(R.id.note_date_btn) protected Button mDateButton;
	@BindView(R.id.note_active_checkbox) protected CheckBox mActiveCheckbox;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
		mNote = Container.instance(getActivity()).getNoteManager().get(noteId);

		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_NOTE_ID, mNote.getId());
		getActivity().setResult(Activity.RESULT_OK, resultIntent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note, container, false);
		ButterKnife.bind(NoteFragment.this, view);
		setViewsFromModel();
		return view;
	}

	@OnTextChanged(R.id.note_title_edit)
	public void onNoteTitleEditChanged(CharSequence s, int start, int before, int count) {
		mNote.setTitle(s.toString());
	}

	@OnCheckedChanged(R.id.note_active_checkbox)
	public void onActiveCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mNote.setActive(isChecked);
	}

	private void setViewsFromModel() {
		mTitleEdit.setText(mNote.getTitle());
		mDateButton.setText(mNote.getFormattedDate());
		mDateButton.setEnabled(false);
		mActiveCheckbox.setChecked(mNote.isActive());
	}

	public static NoteFragment newInstance(UUID id) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_NOTE_ID, id);

		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public static UUID getChangedUUID(int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return null;
		}

		if (!data.hasExtra(EXTRA_NOTE_ID)) {
			return null;
		}

		return (UUID) data.getSerializableExtra(EXTRA_NOTE_ID);
	}
}
