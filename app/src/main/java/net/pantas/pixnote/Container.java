package net.pantas.pixnote;

import android.content.Context;

public class Container {
	private final NoteManager mNoteManager;

	private static Container sInstance;
	public static Container instance(Context context) {
		if (sInstance == null) {
			sInstance = new Container(context);
		}
		return sInstance;
	}

	private Container(Context context) {
		mNoteManager = new NoteManager();
	}

	public NoteManager getNoteManager() {
		return mNoteManager;
	}
}
