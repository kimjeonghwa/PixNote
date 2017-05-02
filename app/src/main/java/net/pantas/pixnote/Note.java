package net.pantas.pixnote;

import java.util.UUID;

public class Note {
	private UUID mId;
	private String mTitle;

	public Note() {
		mId = UUID.randomUUID();
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}
}
