package io.hackathon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.avos.sns.SNS;
import com.avos.sns.SNSType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

public class MainActivity extends AppCompatActivity implements ShareFragment.OnFragmentInteractionListener {
    public static final String STATE_PAGER_POSITION = "pager_position";
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    TabLayout tabLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        tabLayout.addTab(tabLayout.newTab().setText("我被咬了"));
        tabLayout.addTab(tabLayout.newTab().setText("看看别人"));
        tabLayout.addTab(tabLayout.newTab().setText("我要灭蚊"));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt(STATE_PAGER_POSITION));
        } else {
            pager.setCurrentItem(0);
        }
    }

    @DebugLog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, SNSType.AVOSCloudSNSSinaWeibo);

    }

    @Override
    public void onSendFinish() {
        pager.setCurrentItem(1);
    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ShareFragment.newInstance();
                case 1:
                    return LookFragment.newInstance();
                default:
                    return GameFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PAGER_POSITION, pager.getCurrentItem());
    }
}
