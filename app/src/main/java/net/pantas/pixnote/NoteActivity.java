package net.pantas.pixnote;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {
	private static final String EXTRA_NOTE_ID = "net.pantas.pixnote.EXTRA_NOTE_ID";

	@Override
	protected Fragment createFragment() {
		UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
		return NoteFragment.newInstance(noteId);
	}

	public static Intent newIntent(Context packageContext, UUID id) {
		Intent intent = new Intent(packageContext, NoteActivity.class);
		intent.putExtra(EXTRA_NOTE_ID, id);
		return intent;
	}
}
