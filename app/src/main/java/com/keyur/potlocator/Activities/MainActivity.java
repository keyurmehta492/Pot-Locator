package com.keyur.potlocator.Activities;

import com.keyur.potlocator.R;
import com.keyur.potlocator.fragments.Detected;
import com.keyur.potlocator.fragments.Locate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    static int user_id;
    String name;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", -1);
        name = intent.getStringExtra("name");

        Log.d("@@@@ para pass","" + user_id + ", " + name);

    } //onCreate


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    static class SectionsPagerAdapter extends FragmentPagerAdapter {

        Bundle bundle;
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){
                case 0:
                    //Locate locate = new Locate();

                    bundle = new Bundle();
                    Log.d("@@@@ uer_id", "" + user_id );
                    bundle.putInt("userid", user_id);
                    return Locate.getInstance(bundle);

                case 1:
                    Detected detected = new Detected();

                    bundle = new Bundle();
                    Log.d("@@@@ uer_id", "" + user_id );
                    bundle.putInt("userid", user_id);

                    return detected.getInstance(bundle);

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Locate";
                case 1:
                    return "Detected";

            }
            return null;
        }
    }//sectionPageAdaptor

} //class MainActivity
