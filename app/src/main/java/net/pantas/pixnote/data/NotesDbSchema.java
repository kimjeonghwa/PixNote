package net.pantas.pixnote.data;

public class NotesDbSchema {
	public static final class NotesTable {
		public static final String NAME = "notes";

		public static final class Cols {
			public static final String ID = "id";
			public static final String TITLE = "title";
			public static final String DATE = "date";
			public static final String ACTIVE = "active";
		}
	}
}
