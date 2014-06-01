package com.superscript.doodle;

import com.superscipt.doodle.services.DoodleServer;
import com.superscript.doodle.callbacks.ServerCallbacks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity implements ServerCallbacks {

	private static MainActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivity = this;

		startService(new Intent(getApplicationContext(), DoodleServer.class));

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(getApplicationContext(), DoodleServer.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public ServerCallbacks getListener() {
		return this;
	}

	@Override
	public void doodleServiceStarted(Context serviceContext) {

	}

	@Override
	public void doodleServerStarted() {
		App.makeToast("Server started", true);
	}

	@Override
	public void deviceConnected() {
		// TODO Auto-generated method stub

	}

}
