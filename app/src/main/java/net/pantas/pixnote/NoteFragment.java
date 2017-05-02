package net.pantas.pixnote;

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

import java.text.DateFormat;

public class NoteFragment extends Fragment {
	private Note mNote;

	@BindView(R.id.note_title_edit) protected EditText mTitleEdit;
	@BindView(R.id.note_date_btn) protected Button mDateButton;
	@BindView(R.id.note_active_checkbox) protected CheckBox mActiveCheckbox;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNote = new Note();
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
		mDateButton.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(mNote.getDate()));
		mDateButton.setEnabled(false);
		mActiveCheckbox.setChecked(mNote.isActive());
	}
}
