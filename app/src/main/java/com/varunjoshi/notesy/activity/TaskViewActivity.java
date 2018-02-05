package com.varunjoshi.notesy.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Adapters.NotesAdapter;
import com.varunjoshi.notesy.activity.Adapters.SectionsPagerAdapter;
import com.varunjoshi.notesy.activity.Fragments.ShowDoneNotes;
import com.varunjoshi.notesy.activity.Fragments.ShowPendingNotes;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskViewActivity extends AppCompatActivity {

    AppDatabase mAppDatabase;
    NotesAdapter mAdapter;
    NoteDao mNoteDao;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mPager;

    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        ButterKnife.bind(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mTabLayout.setupWithViewPager(mPager);

        mAppDatabase = AppDatabase.getAppDatabase(this);
        mAdapter = new NotesAdapter(new ArrayList<Note>(), this);

        setupViewPager();

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShowPendingNotes(), "CURRENT");
        adapter.addFragment(new ShowDoneNotes(), "DONE");
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);
        mPager.setCurrentItem(0);


    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
