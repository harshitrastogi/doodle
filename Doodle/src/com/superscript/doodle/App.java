package com.superscript.doodle;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class App extends Application {

	private static Context appContext;
	public static final int PORT = 8080;

	public static byte[] image;

	@Override
	public void onCreate() {
		appContext = this;
		super.onCreate();
	}

	public static void makeToast(final String msg, boolean shortOrLong) {

		final int toastDuration;

		if (shortOrLong)
			toastDuration = Toast.LENGTH_SHORT;
		else
			toastDuration = Toast.LENGTH_LONG;

		if (MainActivity.getMainActivity() != null) {

			MainActivity.getMainActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(MainActivity.getMainActivity(), msg,
							toastDuration).show();
				}
			});

		}

	}

	public static void makeToast(final String msg, boolean shortOrLong,
			final Activity activity) {

		final int toastDuration;

		if (shortOrLong)
			toastDuration = Toast.LENGTH_SHORT;
		else
			toastDuration = Toast.LENGTH_LONG;

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, msg, toastDuration).show();
			}
		});

	}

	public static InetSocketAddress getLocalIpAddress(Context context) {

		if (context != null) {

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			InetAddress address = null;

			try {
				address = InetAddress.getByName(String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			InetSocketAddress inetSocket = new InetSocketAddress(address
					.toString().substring(1), PORT);

			return inetSocket;
		} else {
			Log.e("SocketServer", "Context is null. Couldn't get IP Address");
			return null;
		}

	}

}
