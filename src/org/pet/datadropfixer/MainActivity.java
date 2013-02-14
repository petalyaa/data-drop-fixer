package org.pet.datadropfixer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	private static final String TAG_SERVICE = "DataDropFixer_Service";

	private static final String TAG_SETTING = "DataDropFixer_Setting";

	private FragmentActivity parent;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private ContextHolder holder;

	private Menu menu;
	
	private View historyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		holder = ContextHolder.getInstance(getApplicationContext());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						if (position == 1) {
							if (menu != null) {
								for (int i = 0; i < menu.size(); i++) {
									MenuItem menuItem = menu.getItem(i);
									if (menuItem != null) {
										int itemId = menuItem.getItemId();
										switch (itemId) {
										case R.id.menu_clear_history:
											menuItem.setVisible(true);
											break;
										case R.id.menu_refresh_history:
											menuItem.setVisible(true);
											break;
										default:
											break;
										}

									}
								}
							}
						} else {
							if (menu != null) {
								for (int i = 0; i < menu.size(); i++) {
									MenuItem menuItem = menu.getItem(i);
									if (menuItem != null) {
										int itemId = menuItem.getItemId();
										switch (itemId) {
										case R.id.menu_clear_history:
											menuItem.setVisible(false);
											break;
										case R.id.menu_refresh_history:
											menuItem.setVisible(false);
											break;
										default:
											break;
										}

									}
								}
							}
						}

						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		parent = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		FragmentManager fm = getSupportFragmentManager();
		switch (itemId) {
		case R.id.menu_about:
			final AboutPopup popup = new AboutPopup();
			popup.show(fm, "");
			break;
		case R.id.menu_clear_history :
			ClearHistoryAction clearHistoryAction = new ClearHistoryAction(historyView, parent);
			boolean isClearSuccess = clearHistoryAction.clear();
			if(isClearSuccess){
				Toast.makeText(parent, parent.getString(R.string.msg_clear_history_success), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, parent.getString(R.string.msg_clear_history_failed), Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.menu_refresh_history :
			RefreshHistoryAction refreshHistoryAction = new RefreshHistoryAction(historyView, parent);
			boolean isRefreshSuccess = refreshHistoryAction.refresh();
			if(isRefreshSuccess){
				Toast.makeText(parent, parent.getString(R.string.msg_refresh_history_success), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, parent.getString(R.string.msg_refresh_history_failed), Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(Locale.US);
			case 1:
				return getString(R.string.title_section2).toUpperCase(Locale.US);
			case 2:
				return getString(R.string.title_section3).toUpperCase(Locale.US);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int position = getArguments().getInt(ARG_SECTION_NUMBER);
			View view = null;
			if (position == 1) {
				// render service page
				view = inflater
						.inflate(R.layout.service_view, container, false);
				final Button startButton = (Button) view
						.findViewById(R.id.serviceStartButton);
				final Button stopButton = (Button) view
						.findViewById(R.id.serviceStopButton);
				final Intent intent = new Intent(
						parent.getApplicationContext(),
						DataDropFixerService.class);
				boolean isServiceStarted = holder.isServiceStart();
				startButton.setEnabled(false);
				stopButton.setEnabled(false);
				final TextView serviceStatusLabel = (TextView) view
						.findViewById(R.id.serviceStatusLabel);
				final TextView connectionStatusLabel = (TextView) view
						.findViewById(R.id.connectionStatusLabel);
				final TextView connectionTypeLabel = (TextView) view
						.findViewById(R.id.networkTypeLabel);
				DataConnectionManager connectionManager = new DataConnectionManager(
						parent.getApplicationContext());
				if (isServiceStarted) {
					stopButton.setEnabled(true);
					serviceStatusLabel.setText(getString(R.string.started));
				} else {
					startButton.setEnabled(true);
					serviceStatusLabel.setText(getString(R.string.not_started));
				}
				if (connectionManager.isEnabled()) {
					connectionStatusLabel
							.setText(getString(R.string.connected));
				} else {
					connectionStatusLabel
							.setText(getString(R.string.not_connected));
				}
				if (connectionManager.isConnectedThroughWifi()) {
					connectionTypeLabel
							.setText(getString(R.string.network_type_wifi));
				}

				startButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG_SERVICE, "Starting data drop fixer service.");
						parent.startService(intent);
						startButton.setEnabled(false);
						stopButton.setEnabled(true);
						serviceStatusLabel.setText(getString(R.string.started));
					}
				});

				stopButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG_SERVICE, "Stopping data drop fixer service.");
						parent.stopService(intent);
						startButton.setEnabled(true);
						stopButton.setEnabled(false);
						serviceStatusLabel
								.setText(getString(R.string.not_started));
					}
				});

			} else if (position == 2) {
				// render history page
				view = inflater.inflate(R.layout.history_view, container, false);
				
				RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.mainNoHistory);
				HistoryFileReader historyFileReader = new HistoryFileReader();
				ListView listView = (ListView) view.findViewById(R.id.historyListItems);
				ArrayList<HistoricalData> historicalDataList = historyFileReader.getHistoricalDataList();
				if(historicalDataList.size() <= 0){
					relativeLayout.setVisibility(View.VISIBLE);
				} else {
					relativeLayout.setVisibility(View.GONE);
				}
				Collections.sort(historicalDataList);
				HistoricalListAdapter adapter = new HistoricalListAdapter(parent, historicalDataList);
				listView.setAdapter(adapter);
				historyView = view;
			} else if (position == 3) {
				view = inflater
						.inflate(R.layout.setting_view, container, false);

				int currentProgress = holder.getCheckingInterval();
				final SeekBar checkingInterval = (SeekBar) view
						.findViewById(R.id.checkingInterval);
				final TextView checkingIntervalLabel = (TextView) view
						.findViewById(R.id.checkingIntervalLabel);
				final CheckBox checkboxStartOnBoot = (CheckBox) view
						.findViewById(R.id.startOnBoot);
				final Spinner spinnerIntervalType = (Spinner) view
						.findViewById(R.id.checkingIntervalType);
				checkingIntervalLabel.setText(String.valueOf(currentProgress));
				Log.v(TAG_SETTING, "Current progress for checking interval is "
						+ currentProgress);
				checkingInterval.setProgress(currentProgress);
				spinnerIntervalType.setSelection(holder.getIntervalTimeType());
				Log.v(TAG_SETTING,
						"Is start on boot enabled : " + holder.isStartOnBoot());
				checkboxStartOnBoot.setChecked(holder.isStartOnBoot());
				spinnerIntervalType
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> par, View arg1, int pos, long id) {
								String selectedValue = par.getItemAtPosition(pos).toString().toLowerCase(Locale.US);
								Log.v(TAG_SETTING, "Selecting " + selectedValue
										+ " for interval type.");

								int intervalTimeType = ContextHolder.MINUTES;
								long offset = ContextHolder.MINUTES_OFFSET;
								if ("seconds".equals(selectedValue)) {
									if (checkingInterval.getProgress() < 5) {
										Toast.makeText(
												parent.getApplicationContext(),
												"For safety reason, i have set minimum to 5 seconds.",
												Toast.LENGTH_LONG).show();
										checkingInterval.setProgress(5);
										holder.setCheckingInterval(5);
									}
									intervalTimeType = ContextHolder.SECONDS;
									offset = ContextHolder.SECONDS_OFFSET;
								} else if ("minutes".equals(selectedValue)) {
									intervalTimeType = ContextHolder.MINUTES;
									offset = ContextHolder.MINUTES_OFFSET;
								} else if ("hours".equals(selectedValue)) {
									intervalTimeType = ContextHolder.HOURS;
									offset = ContextHolder.HOURS_OFFSET;
								}
								holder.setIntervalTimeType(intervalTimeType);
								holder.setOffset(offset);
								Log.v(TAG_SETTING,
										"Setting interval time type "
												+ intervalTimeType
												+ " and offset " + offset);
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {

							}

						});
				checkboxStartOnBoot
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									Log.v(TAG_SETTING,
											"This will start on boot complete.");
									holder.setStartOnBoot(true);
								} else {
									Log.v(TAG_SETTING,
											"This will not start on boot complete.");
									holder.setStartOnBoot(false);
								}
							}
						});
				checkingInterval
						.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

							private int progress;

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// User done changing interval. Update context
								// Holder.
								if (progress < 5) {
									if (spinnerIntervalType
											.getSelectedItemPosition() == 0) {
										Toast.makeText(
												parent.getApplicationContext(),
												"For safety reason, i have set minimum to 5 seconds.",
												Toast.LENGTH_LONG).show();
										progress = 5;
										checkingInterval.setProgress(progress);
									} else {
										if (progress < 1) {
											String constraints = "minute";
											if (spinnerIntervalType
													.getSelectedItemPosition() == 2)
												constraints = "hour";
											Toast.makeText(
													parent.getApplicationContext(),
													"For safety reason, i have set minimum to 1 "
															+ constraints + ".",
													Toast.LENGTH_LONG).show();
											progress = 1;
											checkingInterval
													.setProgress(progress);
										}
									}
								}

								holder.setCheckingInterval(progress);
								Log.d(TAG_SETTING,
										"Succesfully update checking interval.");
							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// Do nothing??
							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {
								this.progress = progress;
								checkingIntervalLabel.setText(String
										.valueOf(progress));
								if (progress < 5) {
								}
							}
						});
			}
			return view;
		}
	}

}
