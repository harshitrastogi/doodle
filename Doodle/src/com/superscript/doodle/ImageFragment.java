package com.superscript.doodle;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {

	private ImageView imageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_image, container, false);
		imageView = (ImageView) v.findViewById(R.id.image);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Drawable image = null;

		if (App.image != null) {
			image = new BitmapDrawable(getResources(),
					BitmapFactory.decodeByteArray(App.image, 0,
							App.image.length));
		}

		if (image != null) {
			imageView.setImageDrawable(image);
		} else {
			App.makeToast("Final drawable is null", false);
		}

	}
}
