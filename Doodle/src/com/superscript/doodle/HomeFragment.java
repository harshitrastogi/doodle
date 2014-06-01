package com.superscript.doodle;

import java.util.Timer;
import java.util.TimerTask;

import com.superscript.doodle.callbacks.ServerCallbacks;
import com.superscript.doodle.fragments.TextMessagesFragment;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {

	private TextView ip;
	private EditText input;
	private Button connectButton;
	private WebSocketConnection client;
	private String connectionUrl = "ws://";
	private ServerCallbacks listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_home, container, false);
		ip = (TextView) v.findViewById(R.id.tv_ipadd);
		input = (EditText) v.findViewById(R.id.et_ip);
		connectButton = (Button) v.findViewById(R.id.button1);
		connectButton.setOnClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ip.setText(App.getLocalIpAddress(getActivity()).getAddress()
				.getHostAddress()
				+ ":" + App.PORT);
		client = new WebSocketConnection();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		connectionUrl = "ws://" + input.getText().toString();
		try {
			WebSocketOptions options = new WebSocketOptions();
			options.setMaxFramePayloadSize(5000 * 1024);
			options.setMaxMessagePayloadSize(5000 * 1024);
			client.connect(connectionUrl, new MyWebSocketHandler(), options);
		} catch (WebSocketException e) {
			App.makeToast("Couldn't connect. Check ip and try again", false);
			client.disconnect();
			e.printStackTrace();
		}

	}

	public class MyWebSocketHandler extends WebSocketHandler {

		@Override
		public void onOpen() {
			super.onOpen();
			final TextMessagesFragment messenger = MainActivity
					.getMainActivity().getTextMessageFrag();

			DoodleFragmentManager.getInstance().setNextFragment(messenger);
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					messenger.setConnection(client);
					listener = MainActivity.getMainActivity()
							.getServiceObject().getListener();
					listener.deviceConnected(null);
				}
			}, 1000);
		}

		@Override
		public void onClose(int code, String reason) {
			super.onClose(code, reason);

			if (listener != null) {
				listener.deviceDisconnected(null);
			}
		}

		@Override
		public void onTextMessage(String payload) {
			super.onTextMessage(payload);

			if (listener != null) {
				listener.messageReceived(null, payload);
			}

		}

		@Override
		public void onRawTextMessage(byte[] payload) {
			if (listener != null) {
				listener.imageReceived(payload);
			}
			super.onRawTextMessage(payload);
		}

		@Override
		public void onBinaryMessage(byte[] payload) {
			if (listener != null) {
				listener.imageReceived(payload);
			}
			super.onBinaryMessage(payload);
		}

	}

}
