package net.pantas.pixnote;

import java.util.*;

class NoteManager {
	private HashMap<UUID, Note> mNotes;

	NoteManager() {
		mNotes = new HashMap<>();
	}

	ArrayList<Note> list() {
		ArrayList<Note> list = new ArrayList<Note>(mNotes.values());
		Collections.sort(list, new NoteComparator());
		return list;
	}

	Note get(UUID id) {
		return mNotes.get(id);
	}

	void add(Note note) {
		mNotes.put(note.getId(), note);
	}

	int size() {
		return mNotes.size();
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
