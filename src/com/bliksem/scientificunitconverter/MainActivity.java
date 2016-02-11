package com.bliksem.scientificunitconverter;

import java.util.TreeMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;
	private boolean inSettings = false;
	private Integer mDrawerNumber;
	
	DataStore dataStore;
	TreeMap<Integer, String> groupNames = new TreeMap<Integer, String>();
	TreeMap<Integer, String> groupIcons = new TreeMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dataStore = DataStore.getInstance();
		if ( ! dataStore.isInitDone() ) dataStore.init(getApplicationContext());
		
		groupNames = dataStore.getGroupNames();
		groupIcons = dataStore.getGroupIcons();
			
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

		mTitle = getTitle();

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number)
	{
		mDrawerNumber = number - 1;
		mTitle = groupNames.get(mDrawerNumber);
		IconValue v = IconValue.valueOf(groupIcons.get(mDrawerNumber));
		IconDrawable c = new IconDrawable(this,v).colorRes(R.color.ab_item).actionBarSize();
		getActionBar().setIcon(c);
	}

	public void restoreActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		IconValue v = IconValue.valueOf(groupIcons.get(mDrawerNumber));
		IconDrawable c = new IconDrawable(this,v).colorRes(R.color.ab_item).actionBarSize();
		getActionBar().setIcon(c);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		if (inSettings)
		{
			this.backFromSettingsFragment();
			restoreActionBar();
			return;
		}

		finish();

	}

	private void backFromSettingsFragment()
	{
		inSettings = false;
		getFragmentManager().popBackStack();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			inSettings = true;
			getActionBar().setTitle("Settings");
			getActionBar().setIcon(new IconDrawable(this, IconValue.fa_cog).colorRes(R.color.ab_item).actionBarSize());
			getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).addToBackStack("settings").commit();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment
	{

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber)
		{
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity)
		{
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
}
