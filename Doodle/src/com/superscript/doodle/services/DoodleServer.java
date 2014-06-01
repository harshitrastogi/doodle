package com.superscript.doodle.services;

import java.io.IOException;
import java.nio.ByteBuffer;

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

				if (listener != null) {
					listener.deviceConnected(conn);
				}
			}

			@Override
			public void onMessage(WebSocket conn, String msg) {
				if (listener != null) {
					listener.messageReceived(conn, msg);
				}
			}
			
			@Override
			public void onMessage(WebSocket conn, ByteBuffer message) {
				if (listener != null){
					listener.imageReceived(message.array());
				}
				super.onMessage(conn, message);
			}

			@Override
			public void onError(WebSocket conn, Exception ex) {
				if (ex != null && ex.toString() != null)
					Log.e(LOGTAG, ex.toString());
			}

			@Override
			public void onClose(WebSocket conn, int arg1, String arg2,
					boolean arg3) {
				if (listener != null) {
					listener.deviceDisconnected(conn);
				}
			}
		};

		server.start();

		listener = MainActivity.getMainActivity().getListener();

		if (listener != null) {
			listener.doodleServerStarted(this, server);
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

	public ServerCallbacks getListener() {
		return listener;
	}

}
