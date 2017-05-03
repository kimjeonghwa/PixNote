package net.pantas.pixnote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.UUID;

public class NotePagerActivity extends FragmentActivity {
	private static final String EXTRA_NOTE_ID = "net.pantas.pixnote.EXTRA_NOTE_ID";

	@BindView(R.id.activity_note_pager_view_pager)
	ViewPager mViewPager;

	private ArrayList<Note> mNotes;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pager);

		ButterKnife.bind(this);

		mNotes = Container.instance(this).getNoteManager().list();

		mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return NoteFragment.newInstance(mNotes.get(position).getId());
			}

			@Override
			public int getCount() {
				return mNotes.size();
			}
		});

		UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
		for (int i = 0; i < mNotes.size(); i++) {
			if (mNotes.get(i).getId().equals(noteId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	public static Intent newIntent(Context packageContext, UUID id) {
		Intent intent = new Intent(packageContext, NotePagerActivity.class);
		intent.putExtra(EXTRA_NOTE_ID, id);
		return intent;
	}
}
