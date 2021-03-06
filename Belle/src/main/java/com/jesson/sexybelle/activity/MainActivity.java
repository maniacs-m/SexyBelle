package com.jesson.sexybelle.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.jesson.android.widget.Toaster;
import com.jesson.sexybelle.AppConfig;
import com.jesson.sexybelle.R;
import com.jesson.sexybelle.dao.model.Series;
import com.jesson.sexybelle.fragment.NavigationDrawerFragment;
import com.jesson.sexybelle.fragment.PhotoStreamFragment;
import com.jesson.sexybelle.helper.SeriesHelper;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.appwall.GdtAppwall;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Series mSeries;

    private GdtAppwall appwall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initBannerAd();
        initAppWall();

        UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.silentUpdate(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.flush(getApplication());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        List<Series> seriesList = SeriesHelper.getInstance().getSeriesList();
        if (position < 0 || position > seriesList.size() - 1) {
            return;
        }
        mSeries = seriesList.get(position);
        if (mSeries.getType() == -2) {
            appwall.doShowAppWall();
        } else {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            String tag = String.valueOf(mSeries.getType());
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                fragment = PhotoStreamFragment.newInstance(mSeries);
            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
        }
    }

    public void onSectionAttached(Series series) {
        mTitle = series.getTitle();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem item = menu.findItem(R.id.action_refresh);
            if (mSeries != null && mSeries.getType() < 0) {
                item.setVisible(false);
            } else {
                item.setVisible(true);
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_feedback) {
            FeedbackAgent agent = new FeedbackAgent(this);
            agent.startFeedbackActivity();
            return true;
        } else if (item.getItemId() == R.id.action_update) {
            UmengUpdateAgent.setUpdateListener(mUmengUpdateListener);
            UmengUpdateAgent.forceUpdate(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBannerAd() {
        RelativeLayout l = (RelativeLayout) findViewById(R.id.ad_content);
        AdView adv = new AdView(this, AdSize.BANNER, AppConfig.GDT_AD_APPID, AppConfig.GDT_AD_BANNER_POSID);
        l.addView(adv);
        AdRequest adr = new AdRequest();
        adr.setShowCloseBtn(true);
        adr.setTestAd(AppConfig.DEBUG);
        adr.setRefresh(31);
        adv.fetchAd(adr);
    }

    private void initAppWall() {
        appwall = new GdtAppwall(this, AppConfig.GDT_AD_APPID,
                AppConfig.GDT_AD_APPWALL_POSID, AppConfig.DEBUG);
    }

    public void onRefresh() {
        if (mSeries != null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("type", String.valueOf(mSeries.getType()));
            map.put("title", mSeries.getTitle());
            map.put("mode", String.valueOf(AppConfig.SERIES_MODE));
            MobclickAgent.onEvent(this, "Refresh", map);
        }
    }

    private UmengUpdateListener mUmengUpdateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int i, UpdateResponse updateResponse) {
            switch (i) {
                case UpdateStatus.Yes: // has update
                    break;
                case UpdateStatus.No: // has no update
                    Toaster.show(MainActivity.this, "当前已是最新版本~");
                    break;
                case UpdateStatus.NoneWifi: // none wifi
                    Toaster.show(MainActivity.this, "没有wifi连接， 只在wifi下更新");
                    break;
                case UpdateStatus.Timeout: // time out
                    Toaster.show(MainActivity.this, "获取数据失败，请稍后重试");
                    break;
            }
        }
    };

}