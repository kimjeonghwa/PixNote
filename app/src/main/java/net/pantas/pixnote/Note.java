package net.pantas.pixnote;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Note {
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mActive;

	public Note() {
		this(UUID.randomUUID());
	}

	public Note(UUID id) {
		mId = id;
		mDate = new Date();
		mActive = true;
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

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isActive() {
		return mActive;
	}

	public void setActive(boolean active) {
		mActive = active;
	}

	public String getFormattedDate() {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(getDate());
	}
}
