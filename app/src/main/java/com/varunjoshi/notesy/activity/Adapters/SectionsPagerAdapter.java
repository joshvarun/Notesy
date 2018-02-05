package com.varunjoshi.notesy.activity.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.varunjoshi.notesy.activity.Fragments.ShowDoneNotes;
import com.varunjoshi.notesy.activity.Fragments.ShowPendingNotes;

/**
 * Notesy
 * Created by Varun Joshi on Mon, {5/2/18}.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new ShowPendingNotes();
            case 1:
                return new ShowDoneNotes();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current";
            case 1:
                return "Done";
        }
        return null;
    }
}
