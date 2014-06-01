package com.superscript.doodle.fragments;

import org.java_websocket.WebSocket;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.superscript.doodle.App;
import com.superscript.doodle.R;

public class TextMessagesFragment extends Fragment implements OnClickListener {

	TextView msgReceived, ipAddress;
	EditText newMsg;
	Button sendButton;
	private WebSocket connection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.frag_textmsg, container, false);

		msgReceived = (TextView) v.findViewById(R.id.tv_lastmsg);
		newMsg = (EditText) v.findViewById(R.id.et_msg);
		msgReceived.setText("<No message received yet>");
		sendButton = (Button) v.findViewById(R.id.btn_send);
		sendButton.setOnClickListener(this);
		ipAddress = (TextView) v.findViewById(R.id.tv_ipaddress);
		ipAddress.setText(App.getLocalIpAddress(getActivity()).getAddress()
				.getHostAddress());
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public void setConnection(WebSocket connection) {
		this.connection = connection;
	}

	public void messageReceived(final String message) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				msgReceived.setText(message);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (connection != null) {
			connection.send(newMsg.getText().toString());
		}
	}

}
