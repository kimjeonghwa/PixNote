package net.pantas.pixnote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class NoteFragment extends Fragment {
	private Note mNote;

	@BindView(R.id.note_title_edit)
	private EditText mNoteTitleEdit;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNote = new Note();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@OnTextChanged(R.id.note_title_edit)
	public void onNoteTitleEditChanged(CharSequence s, int start, int before, int count) {
		mNote.setTitle(s.toString());
	}
}
