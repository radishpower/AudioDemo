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
	public static boolean isPlaying = false;
	public static int curFrame = -1;
	public static int[] ids;
	public static int viewMode = VIEW_MODE_RGBA;
	public static int numFiles = 8;
	public static long curTime = 0;

	//public static final String AUDIO_DIR = "/Android/org.opencv.samples.radishower/sound_samples";
	public static final String AUDIO_DIR = "/Audio Demo/sound_samples/";

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
			// initialize soundbox
			box = new SoundBox();
			String[] files = new String[numFiles];
			ids = new int[numFiles];
			for (int i = 0; i<numFiles; ++i){
				files[i] = Environment.getExternalStorageDirectory() +
								AudioDemoActivity.AUDIO_DIR + String.format("%02d", i) + ".wav";
				ids[i] = box.load(files[i]);
			}
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
		mItemPreviewAUDIO = menu.add("Preview Audio");
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
		for (int i = 0; i<numFiles; ++i){
			box.pause(ids[i]);
		}
	}
}
