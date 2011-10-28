package org.opencv.samples.radishower;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SoundActivity extends Activity {
	static final String TAG = "SoundActivity";
	static int musicID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audioplaying);
		TextView tv = new TextView(this);
		tv.setText("The current value: " + musicID);
		setContentView(tv);
		SoundBox box = AudioDemoActivity.box;
		for (int i = 0; i < 4; i++) {
			box.pause(i);
		}
		box.play(musicID % 4);
		setContentView(tv);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}