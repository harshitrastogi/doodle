package com.superscript.doodle.fragments;

import org.java_websocket.WebSocket;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.superscript.doodle.App;
import com.superscript.doodle.R;

import de.tavendo.autobahn.WebSocketConnection;

public class TextMessagesFragment extends Fragment implements OnClickListener {

	private TextView msgReceived, ipAddress;
	private EditText newMsg;
	private Button sendButton;
	private WebSocket serverConnection;
	private WebSocketConnection clientConnection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.frag_textmsg, container, false);

		msgReceived = (TextView) v.findViewById(R.id.tv_lastmsg);
		msgReceived.setMovementMethod(new ScrollingMovementMethod());
		newMsg = (EditText) v.findViewById(R.id.et_msg);
		sendButton = (Button) v.findViewById(R.id.btn_send);
		sendButton.setOnClickListener(this);
		if (serverConnection == null && clientConnection == null) {
			sendButton.setEnabled(false);
		}
		ipAddress = (TextView) v.findViewById(R.id.tv_ipaddress);
		ipAddress.setText(App.getLocalIpAddress(getActivity()).getAddress()
				.getHostAddress());
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public void setConnection(Object connection) {

		if (connection instanceof WebSocketConnection) {
			clientConnection = (WebSocketConnection) connection;
		} else if (connection instanceof WebSocket) {
			serverConnection = (WebSocket) connection;
		}

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				sendButton.setText("Send");
				sendButton.setEnabled(true);
			}
		});
	}

	public void messageReceived(final String message) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				String msg = msgReceived.getText() + "\n" + message;
				msgReceived.setText(msg);
				final Layout layout = msgReceived.getLayout();
				if (layout != null) {
					int scrollDelta = layout.getLineBottom(msgReceived
							.getLineCount() - 1)
							- msgReceived.getScrollY()
							- msgReceived.getHeight();
					if (scrollDelta > 0)
						msgReceived.scrollBy(0, scrollDelta);
				}
			}
		});
	}

	public void connectionLost() {
		this.clientConnection = null;
		this.serverConnection = null;

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				sendButton.setEnabled(false);
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (clientConnection != null) {
			clientConnection.disconnect();
		} else if (serverConnection != null) {
			serverConnection.close(0);
		}
	}

	@Override
	public void onClick(View v) {

		if (clientConnection != null) {
			clientConnection.sendTextMessage(newMsg.getText().toString());
		} else if (serverConnection != null) {
			serverConnection.send(newMsg.getText().toString());
		}

	}

}
