package net.pantas.pixnote;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import net.pantas.pixnote.data.NotesDbSchema;
import net.pantas.pixnote.data.NotesDbSchema.NotesTable;
import net.pantas.pixnote.data.PixNoteBaseHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

class NoteManager {
	private Context mContext;
	private SQLiteDatabase mDatabase;

	NoteManager(Context context) {
		mContext = context;
		mDatabase = new PixNoteBaseHelper(mContext).getWritableDatabase();
	}

	private static ContentValues getContentValues(Note note) {
		ContentValues values = new ContentValues();
		values.put(NotesTable.Cols.ID, note.getId().toString());
		values.put(NotesTable.Cols.TITLE, note.getTitle());
		values.put(NotesTable.Cols.DATE, note.getDate().getTime());
		values.put(NotesTable.Cols.ACTIVE, note.isActive() ? 1 : 0);
		return values;
	}

	ArrayList<Note> list() {
		return queryNotes(null, null).readIntoListAndClose();
	}

	Note get(UUID id) {
		ArrayList<Note> results = queryNotes(
			NotesTable.Cols.ID + " = ?",
			new String[]{id.toString()},
			1
		).readIntoListAndClose();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	void add(Note note) {
		ContentValues values = getContentValues(note);
		mDatabase.insert(NotesTable.NAME, null, values);
	}

	void update(Note note) {
		ContentValues values = getContentValues(note);
		mDatabase.update(NotesTable.NAME, values,
			NotesTable.Cols.ID + " = ?",
			new String[]{note.getId().toString()});
	}

	int size() {
		Cursor cursor = mDatabase.rawQuery("select count(*) from " + NotesTable.NAME, null);
		try {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		finally {
			cursor.close();
		}
	}

	private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
		return queryNotes(whereClause, whereArgs, 0);
	}

	private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs, int limit) {
		@SuppressLint("Recycle") Cursor cursor = mDatabase.query(
			NotesTable.NAME,
			null,
			whereClause,
			whereArgs,
			null,
			null,
			null,
			limit > 0 ? String.valueOf(limit) : null
		);

		return new NoteCursorWrapper(cursor);
	}

	private class NoteCursorWrapper extends CursorWrapper {
		public NoteCursorWrapper(Cursor cursor) {
			super(cursor);
		}

		public Note getNote() {
			String idString = getString(getColumnIndex(NotesTable.Cols.ID));
			String title = getString(getColumnIndex(NotesTable.Cols.TITLE));
			long date = getLong(getColumnIndex(NotesTable.Cols.DATE));
			int active = getInt(getColumnIndex(NotesTable.Cols.ACTIVE));

			Note note = new Note(UUID.fromString(idString));
			note.setTitle(title);
			note.setDate(new Date(date));
			note.setActive(active != 0);

			return note;
		}

		public ArrayList<Note> readIntoListAndClose() {
			ArrayList<Note> notes = new ArrayList<>();
			try {
				this.moveToFirst();
				while (!isAfterLast()) {
					notes.add(getNote());
					moveToNext();
				}
			} finally {
				close();
			}
			return notes;
		}
	}

	private class NoteComparator implements Comparator<Note> {
		@Override
		public int compare(Note o1, Note o2) {
			if (o1.getTitle() == null) {
				return 1;
			}
			if (o2.getTitle() == null) {
				return -1;
			}
			return o1.getTitle().compareTo(o2.getTitle());
		}

	}
}
