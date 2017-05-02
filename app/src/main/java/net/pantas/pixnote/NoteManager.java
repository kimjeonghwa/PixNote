package net.pantas.pixnote;

import java.util.*;

class NoteManager {
	private HashMap<UUID, Note> mNotes;

	NoteManager() {
		mNotes = new HashMap<>();

		// TODO: Testing
		for (int i = 0; i < 100; i++) {
			Note note = new Note();
			note.setActive(i % 2 == 0);
			note.setTitle("Note #" + i);
			mNotes.put(note.getId(), note);
		}
	}

	ArrayList<Note> list() {
		ArrayList<Note> list = new ArrayList<Note>(mNotes.values());
		Collections.sort(list, new NoteComparator());
		return list;
	}

	private class NoteComparator implements Comparator<Note> {
		@Override
		public int compare(Note o1, Note o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	}

	public Note get(UUID id) {
		return mNotes.get(id);
	}
}
