package com.superscript.doodle.services;

import java.io.IOException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.superscript.doodle.App;
import com.superscript.doodle.MainActivity;
import com.superscript.doodle.callbacks.ServerCallbacks;

public class DoodleServer extends Service {

	private WebSocketServer server;
	private ServerCallbacks listener;
	private static final String LOGTAG = "DoodleServer";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		server = new WebSocketServer(App.getLocalIpAddress(this)) {

			@Override
			public void onOpen(WebSocket conn, ClientHandshake arg1) {
				App.makeToast("Device connected", true);
			}

			@Override
			public void onMessage(WebSocket conn, String msg) {
				App.makeToast(msg, true);
			}

			@Override
			public void onError(WebSocket conn, Exception ex) {
				Log.e(LOGTAG, ex.getLocalizedMessage());
			}

			@Override
			public void onClose(WebSocket conn, int arg1, String arg2,
					boolean arg3) {
				App.makeToast(arg2, true);
			}
		};

		server.start();

		listener = MainActivity.getMainActivity().getListener();

		if (listener != null) {
			listener.doodleServerStarted();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (server != null) {
			try {
				server.stop();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
