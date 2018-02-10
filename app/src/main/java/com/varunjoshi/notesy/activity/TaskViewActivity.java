package com.varunjoshi.notesy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Adapters.SectionsPagerAdapter;
import com.varunjoshi.notesy.activity.Fragments.ShowDoneNotes;
import com.varunjoshi.notesy.activity.Fragments.ShowPendingNotes;
import com.varunjoshi.notesy.activity.Util.FontFamily;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskViewActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    public static Drawable getTintedDrawable(@NonNull Context context, @NonNull Drawable inputDrawable, @ColorInt int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        ButterKnife.bind(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mTabLayout.setupWithViewPager(mPager);

        Toolbar toolbar = findViewById(R.id.toolbar_TaskViewActivity);
        //  TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable overflowIcon = toolbar.getOverflowIcon();
        toolbar.setOverflowIcon(getTintedDrawable(this, overflowIcon, 0xffffffff));


        FontFamily fontFamily = new FontFamily(this);
        // fontFamily.setMediumFont(toolbar_title);
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShowPendingNotes(), "Pending");
        adapter.addFragment(new ShowDoneNotes(), "Completed");
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT,
                            "Notesy: Simplify your notes & set easy reminders! https://play.google.com/store/apps/details?id=com.varunjoshi.notesy");
                    startActivity(Intent.createChooser(i, "Share App Via"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
        }
        return false;
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
}
