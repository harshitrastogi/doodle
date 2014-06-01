package com.superscript.doodle.callbacks;

import android.content.Context;

public interface ServerCallbacks {

	public void doodleServiceStarted(Context serviceContext);

	public void doodleServerStarted();

	public void deviceConnected();

}
