package com.infamous.site1.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;


import com.infamous.site1.R;
import com.infamous.site1.adapter.DrawerAdapter;
import com.infamous.site1.dialog.Multi_Dialog;
import com.infamous.site1.dialog.Radio_Dialog;
import com.infamous.site1.dialog.Single_Dialog;
import com.infamous.site1.dialog.Standard_Dialog;
import com.infamous.site1.fragments.Blog_Fragment;
import com.infamous.site1.fragments.Bookmark_Fragment;
import com.infamous.site1.fragments.Home_Fragment;
import com.infamous.site1.fragments.Extras_Fragment;
import com.infamous.site1.ui.Items;
import com.infamous.site1.ui.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.io.*;


public class MainActivity extends ActionBarActivity {

    private String[] mDrawerTitles;
    private TypedArray mDrawerIcons;
    private ArrayList<Items> drawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private static FragmentManager mManager;
	private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        mManager = getSupportFragmentManager();

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
        mDrawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);
        drawerItems = new ArrayList<Items>();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        for (int i = 0; i < mDrawerTitles.length; i++) {
            drawerItems.add(new Items(mDrawerTitles[i], mDrawerIcons.getResourceId(i, -(i + 1))));
        }

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header,
                mDrawerList, false);

        final ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer,
                mDrawerList, false);

        // Give your Toolbar a subtitle!
        /* mToolbar.setSubtitle("Subtitle"); */

        mDrawerList.addHeaderView(header, null, true); // true = clickable
        mDrawerList.addFooterView(footer, null, true); // true = clickable

        //Set width of drawer
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        lp.width = calculateDrawerWidth();
        mDrawerList.setLayoutParams(lp);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter(getApplicationContext(), drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
	
	/************************************************************************
	 ******************** This is your Changelog Stuff **********************
	 ************************************************************************/
	public void checkBuild() {
		int buildNum = prefs.getInt("Build Number", 1);
		int currentVersion = 0;

		try {
			currentVersion = getPackageManager()
	    		.getPackageInfo(getPackageName(), 0).versionCode;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	    if(currentVersion > buildNum) {
			getChangelog().show();
			Editor editor = prefs.edit();
			editor.putInt("Build Number", currentVersion);
			editor.commit();
		}

		
		try
		{
			Process p = Runtime.getRuntime().exec("su");
		}
		catch (IOException e)
		{}

	}

	public Dialog getChangelog()
	{
	 	final Dialog CDialog = new Dialog(MainActivity.this);
	 	CDialog.setTitle(getResources().getString(R.string.changelog_title));
	 	CDialog.setContentView(R.layout.changelog);
	 	CDialog.setCanceledOnTouchOutside(true);
	 	CDialog.setCancelable(true);

	 	Button Close = (Button) CDialog.findViewById(R.id.close);
	 	Close.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					CDialog.dismiss();
				}
			});

	 	return CDialog;
	}
	

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                //Account
                showMyDialog("Infamous", "Welcome to our official Infamous Development application!", "cancel", "ok");
                break;
            case 1:
                //Beiträge
                fragment = new Home_Fragment();
                break;
            case 2:
                //Angebote
                fragment = new Extras_Fragment();
                break;
            case 3:
                //Blog
                fragment = new Blog_Fragment();
                break;
            case 4:
                //Favoriten
                fragment = new Bookmark_Fragment();
                break;
            case 5:
                //Google+ Community
                fragment = new Home_Fragment();
                break;
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        if (position != 0) {
            setTitle(mDrawerTitles[position - 1]);
            updateView(position, position, true);
        }
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int calculateDrawerWidth() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        Display display = getWindowManager().getDefaultDisplay();
        int width;
        int height;
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();  // deprecated
            height = display.getHeight();  // deprecated
        }
        return width - actionBarHeight;
    }

    private void updateView(int position, int counter, boolean visible) {

        View v = mDrawerList.getChildAt(position -
                mDrawerList.getFirstVisiblePosition());
        TextView someText = (TextView) v.findViewById(R.id.item_new);
        Resources res = getResources();
        String articlesFound = "";
/*
        switch (position) {
            case 1:
                articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
                someText.setBackgroundResource(R.drawable.new_apps);
                break;
            case 2:
                articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
                someText.setBackgroundResource(R.drawable.new_sales);
                break;
            case 3:
                articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
                someText.setBackgroundResource(R.drawable.new_blog);
                break;
            case 4:
                articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
                someText.setBackgroundResource(R.drawable.new_bookmark);
                break;
            case 5:
                articlesFound = res.getQuantityString(R.plurals.numberOfNewArticles, counter, counter);
                someText.setBackgroundResource(R.drawable.new_community);
                break;
        } */

        someText.setText(articlesFound);
        if (visible) someText.setVisibility(View.VISIBLE);
    }
/*
    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }

    private void trySetupSwipeRefresh() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Toast.makeText(getApplication(),"Refresh!", Toast.LENGTH_LONG);
                }
            });

            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
                mswrl.setCanChildScrollUpCallback(this);
            }
        }
    } */

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void circleIn(View view) {

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void circleOut(final View view) {

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth();

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    /**
     * Sets the components of the standard dialog.
     *
     * @param title Title of the dialog
     * @param message Message of the dialog
     * @param negativeButton Text of negative Button
     * @param positiveButton Text of postive Button
     */
    public static void showMyDialog(String title, String message, String negativeButton, String positiveButton) {
        Standard_Dialog newDialog = Standard_Dialog.newInstance(title, message, negativeButton, positiveButton);
        newDialog.show(mManager, "dialog");
    }

    /**
     * Sets the components of the traditional single-choice dialog.
     *
     * @param title Title of the dialog
     * @param dialogItems Content of the dialog
     * @param negativeButton Text of negative Button
     * @param positiveButton Text of postive Button
     */
    public static void showMySingleDialog(String title, ArrayList<String> dialogItems, String negativeButton, String positiveButton) {
        Single_Dialog newDialog = Single_Dialog.newInstance(title, dialogItems, negativeButton, positiveButton);
        newDialog.show(mManager, "dialog");
    }

    /**
     * Sets the components of the persistent single-choice dialog.
     *
     * @param title Title of the dialog
     * @param dialogItems Content of the dialog
     * @param negativeButton Text of negative Button
     * @param positiveButton Text of postive Button
     */
    public static void showMyRadioDialog(String title, ArrayList<String> dialogItems, String negativeButton, String positiveButton) {
        Radio_Dialog newDialog = Radio_Dialog.newInstance(title, dialogItems, negativeButton, positiveButton);
        newDialog.show(mManager, "dialog");
    }

    /**
     * Sets the components of the persistent multiple-choice dialog.
     *
     * @param title Title of the dialog
     * @param dialogItems Content of the dialog
     * @param negativeButton Text of negative Button
     * @param positiveButton Text of postive Button
     */
    public static void showMyMultiDialog(String title, ArrayList<String> dialogItems, String negativeButton, String positiveButton) {
        Multi_Dialog newDialog = Multi_Dialog.newInstance(title, dialogItems, negativeButton, positiveButton);
        newDialog.show(mManager, "dialog");
    }

}
