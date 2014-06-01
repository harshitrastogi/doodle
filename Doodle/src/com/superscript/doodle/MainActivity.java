package com.superscript.doodle;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.superscript.doodle.callbacks.ServerCallbacks;
import com.superscript.doodle.fragments.TextMessagesFragment;
import com.superscript.doodle.services.DoodleClient;
import com.superscript.doodle.services.DoodleServer;

import de.tavendo.autobahn.WebSocketConnection;

public class MainActivity extends Activity implements ServerCallbacks, Observer {

	private static MainActivity mainActivity;
	private DoodleServer serverService;
	private DoodleClient clientService;
	private WebSocketServer server;
	private WebSocketConnection client;
	private TextMessagesFragment messenger;
	private FragmentManager manager = getFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivity = this;
		messenger = new TextMessagesFragment();

		startService(new Intent(getApplicationContext(), DoodleServer.class));

		DoodleFragmentManager.getInstance().addObserver(this);

		if (savedInstanceState == null) {
			manager.beginTransaction()
					.add(R.id.container,
							DoodleFragmentManager.getInstance()
									.getActiveFragment()).commit();
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

	@Override
	public void onBackPressed() {

		if (DoodleFragmentManager.getInstance().getActiveFragment() instanceof HomeFragment) {
			finish();
		} else {
			DoodleFragmentManager.getInstance().getPreviousFragment();
			super.onBackPressed();
		}

	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public ServerCallbacks getListener() {
		return this;
	}

	@Override
	public void doodleServerStarted(DoodleServer serviceContext,
			WebSocketServer server) {
		this.serverService = serviceContext;
		this.server = server;
	}

	@Override
	public void deviceConnected(final WebSocket connection) {
		DoodleFragmentManager.getInstance().setNextFragment(messenger);
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				messenger.setConnection(connection);
			}
		}, 1000);
	}

	@Override
	public void messageReceived(WebSocket connection, String message) {
		if (messenger.isVisible()) {
			messenger.messageReceived(message);
		}
	}

	@Override
	public void deviceDisconnected(WebSocket connection) {
		if (messenger.isVisible()) {
			messenger.connectionLost();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof Fragment) {
			Fragment fragment = (Fragment) data;
			setActiveFragment(fragment);
		}
	}

	private void setActiveFragment(Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public TextMessagesFragment getTextMessageFrag() {
		return messenger;
	}

	public DoodleServer getServiceObject() {
		return serverService;
	}

}
