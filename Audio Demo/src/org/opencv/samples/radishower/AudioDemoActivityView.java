package org.opencv.samples.radishower;

import org.opencv.android.Utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;

class AudioDemoActivityView extends AudioDemoActivityBase {
	private Mat mYuv;
	private Mat mRgba;
	private Mat mGraySubmat;
	private Mat mIntermediateMat;

	Process processingobject = new Process();

	public AudioDemoActivityView(Context context) {
		super(context);
	}

	@Override
	public void surfaceChanged(SurfaceHolder _holder, int format, int width,
			int height) {
		super.surfaceChanged(_holder, format, width, height);

		synchronized (this) {
			// initialize Mats before usage
			mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2,
					getFrameWidth(), CvType.CV_8UC1);
			mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

			mRgba = new Mat();
			mIntermediateMat = new Mat();
		}
	}

	void playSound(int value) {
		// too lazy to figure out a more proper way
		// 1) send the url to the webview activity using static variable which
		// WebViewActivity read onResume
		// 2) index.html includes javascript which looks at window.location.hash
		// to get the value
		// see assets/index.html for details
		// NOTE: every time you change assets/index.html you need to force
		// rebuild of the apk by going to
		// Project -> Clean... -> select this project or all
		SoundActivity.musicID = value;
		this.getContext().startActivity(
				new Intent(this.getContext(), SoundActivity.class));
	}

	@Override
	protected Bitmap processFrame(byte[] data) {
		mYuv.put(0, 0, data);
		SoundBox box = AudioDemoActivity.box;
		switch (AudioDemoActivity.viewMode) {
		case AudioDemoActivity.VIEW_MODE_AUDIO:
			Process.Result result = processingobject.process(mYuv,
					getFrameHeight(), getFrameWidth(), mRgba);
			if (result != null && result.threshold >= 500.0) {
				// we got the byte!
				Log.e("TAG", String.format("We got byte %d", result.value));
				if (result.value < 1000) {
					int value = result.value;
					String file = String.format("%02d.wav", value
							% AudioDemoActivity.numFiles);
					if (!AudioDemoActivity.isPlaying) {
						Log.e("TAG", String.format(
								"Current frame %d, Start playing %s",
								AudioDemoActivity.curFrame, file));
						AudioDemoActivity.isPlaying = true;
						AudioDemoActivity.curFrame = value
								% AudioDemoActivity.numFiles;
						for (int i = 0; i < AudioDemoActivity.numFiles; ++i) {
							if (i != AudioDemoActivity.curFrame) {
								box.stop(i);
							}
						}
						box.play(AudioDemoActivity.curFrame);
					} else if (AudioDemoActivity.isPlaying
							&& AudioDemoActivity.curFrame != value % 2) {
						Log.e("TAG", String.format(
								"Current frame %d, Switch to playing %s",
								AudioDemoActivity.curFrame, file));
						AudioDemoActivity.curFrame = value
								% AudioDemoActivity.numFiles;
						for (int i = 0; i < AudioDemoActivity.numFiles; ++i) {
							if (i != AudioDemoActivity.curFrame) {
								box.stop(i);
							}
						}
						box.play(AudioDemoActivity.curFrame);
					}
				} else {
					Log.e("TAG", String.format("Stop playing %02d.wav",
							AudioDemoActivity.curFrame));
					AudioDemoActivity.isPlaying = false;
					for (int i = 0; i < AudioDemoActivity.numFiles; ++i) {
						box.stop(i);
					}
				}
			} else {
				if (AudioDemoActivity.isPlaying) {
					Log.e("TAG", String.format("Stop playing %02d.wav",
							AudioDemoActivity.curFrame));
					AudioDemoActivity.isPlaying = false;
					for (int i = 0; i < AudioDemoActivity.numFiles; ++i) {
						box.stop(i);
					}
				}
			}
			break;
		case AudioDemoActivity.VIEW_MODE_RGBA:
			Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
			Core.putText(mRgba, "Synaesthesia Demo", new Point(10, 100),
					3/* CV_FONT_HERSHEY_COMPLEX */, 1,
					new Scalar(255, 0, 0, 255), 3);
			break;
		}

		Bitmap bmp = Bitmap.createBitmap(getFrameWidth(), getFrameHeight(),
				Bitmap.Config.ARGB_8888);

		if (Utils.matToBitmap(mRgba, bmp))
			return bmp;

		bmp.recycle();
		return null;
	}

	@Override
	public void run() {
		super.run();

		synchronized (this) {
			// Explicitly deallocate Mats
			if (mYuv != null)
				mYuv.release();
			if (mRgba != null)
				mRgba.release();
			if (mGraySubmat != null)
				mGraySubmat.release();
			if (mIntermediateMat != null)
				mIntermediateMat.release();

			mYuv = null;
			mRgba = null;
			mGraySubmat = null;
			mIntermediateMat = null;
		}
	}
}