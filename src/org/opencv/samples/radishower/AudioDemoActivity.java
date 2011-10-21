package org.opencv.samples.radishower;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class AudioDemoActivity extends Activity {
	private static final String TAG = "Sample::Activity";

	public static final int VIEW_MODE_RGBA = 0;
	public static final int VIEW_MODE_AUDIO = 1;

	private MenuItem mItemPreviewRGBA;
	private MenuItem mItemPreviewAUDIO;

	public static MediaPlayer player;
	static SoundBox box = null;
	public static int[] ids;
	public static int viewMode = VIEW_MODE_RGBA;

	//public static final String AUDIO_DIR = "/Android/org.opencv.samples.radishower/sound_samples";
	public static final String AUDIO_DIR = "/sound_samples/";

	public AudioDemoActivity() {
		Log.i(TAG, "Instantiated new " + this.getClass());
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new AudioDemoActivityView(this));

		if (box == null) {
			// iknitialize soundbox
			box = new SoundBox();
			String[] files = {
					Environment.getExternalStorageDirectory()
							+ AudioDemoActivity.AUDIO_DIR + "00.wav",
					Environment.getExternalStorageDirectory()
							+ AudioDemoActivity.AUDIO_DIR + "01.wav",
					Environment.getExternalStorageDirectory()
							+ AudioDemoActivity.AUDIO_DIR + "02.wav",
					Environment.getExternalStorageDirectory()
							+ AudioDemoActivity.AUDIO_DIR + "03.wav"};

			ids = new int[files.length];
			ids[0] = box.load(files[0]);
			ids[1] = box.load(files[1]);
			ids[2] = box.load(files[2]);
			ids[3] = box.load(files[3]);
		}
		/*
		 * player = new MediaPlayer(); try {
		 * player.setDataSource(Environment.getExternalStorageDirectory() +
		 * AudioDemoActivity.AUDIO_DIR + "/" +"out.wav"); player.prepare();
		 * 
		 * } catch (IllegalArgumentException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IllegalStateException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } //player.pause();
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu");
		mItemPreviewRGBA = menu.add("Preview RGBA");
		mItemPreviewAUDIO = menu.add("Preview GRAY");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "Menu Item selected " + item);
		if (item == mItemPreviewRGBA)
			viewMode = VIEW_MODE_RGBA;
		else if (item == mItemPreviewAUDIO)
			viewMode = VIEW_MODE_AUDIO;
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		box.pause(0);
		box.pause(1);
		box.pause(2);
		box.pause(3);
	}
}
