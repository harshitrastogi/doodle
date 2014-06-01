package com.superscript.doodle.callbacks;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import com.superscript.doodle.services.DoodleServer;

public interface ServerCallbacks {

	public void doodleServerStarted(DoodleServer serviceContext,
			WebSocketServer server);

	public void deviceConnected(WebSocket connection);

	public void messageReceived(WebSocket connection, String message);

	public void imageReceived(byte[] imageArray);

	public void deviceDisconnected(WebSocket connection);

}
