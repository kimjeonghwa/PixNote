package net.pantas.pixnote.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.MessageFormat;
import net.pantas.pixnote.data.NotesDbSchema.NotesTable;

public class PixNoteBaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "pixnote.db";

	public PixNoteBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + NotesTable.NAME + "("
				+ NotesTable.Cols.ID + " text primary key, "
				+ NotesTable.Cols.TITLE + ", "
				+ NotesTable.Cols.DATE + ", "
				+ NotesTable.Cols.ACTIVE + ")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
