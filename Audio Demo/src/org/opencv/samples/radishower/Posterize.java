package org.opencv.samples.radishower;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.radishower.Process.Result;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class Posterize {
	private Mat localYUV = new Mat();
	private Mat mRgba = new Mat();
	private Mat mGraySubmat = new Mat();
	private Mat bwimg = new Mat();
	
	public void process(Mat mYuv, int width, int height, Mat output) {
		localYUV = mYuv.clone();
		mGraySubmat = mYuv.submat(0, width, 0, height);
		int block_size = 9;
		int delta = 0;
		
		/*
		Mat fruits = null;
		try {
		    fruits = Utils.loadResource(context, R.drawable.fruits,
		Highgui.CV_LOAD_IMAGE_COLOR);
		} catch (IOException e) {
		    return;
		}

		Mat resized = new Mat();
		Imgproc.resize(fruits, resized, new Size(getWidth(), getHeight()));
		Mat dst = new Mat();
		Imgproc.pyrMeanShiftFiltering(resized, dst, 10, 20, 2);
		Mat img2show = new Mat();
		Imgproc.cvtColor(dst, img2show, Imgproc.COLOR_BGR2RGBA);
		Bitmap bmp = Bitmap.createBitmap(img2show.width(), img2show.height(),
		Config.ARGB_8888);
		if (!Utils.matToBitmap(img2show, bmp))
		    return;
		canvas.drawBitmap(bmp, 0, 0, null); 
		*/
		
		Mat original = new Mat();
		Imgproc.cvtColor(mYuv, original, Imgproc.COLOR_YUV420sp2RGB, 3);
		
		if (!AudioDemoActivityView.cache.empty()){
			int[][] diff = new int[original.height()/32][original.width()/32];
			double mean = 0.0;
			for (int r = 0; r < original.height(); r += 32){
				for (int c = 0; c < original.width(); c += 32){
					diff[r/32][c/32] = (int) Math.abs(original.get(r, c)[0] - AudioDemoActivityView.cache.get(r, c)[0]);
					mean += diff[r/32][c/32];
				}
			}
			mean /= (double)(((double)diff.length) * diff[0].length);
			double std = 0.0;
			for (int i = 0; i < diff.length; ++i){
				for (int j = 0; j < diff[i].length; ++j){
					std += Math.pow(diff[i][j] - mean, 2);
				}
			}
			std = Math.sqrt(std/(((double)diff.length) * diff[0].length));
			System.out.println(std);
			Mat cloned = new Mat();
			//threshold for scene change
			if (std >= 52.0){
				Mat resized = new Mat();
				Imgproc.resize(original, resized, new Size(original.width(), original.height()));
				
				
				Imgproc.pyrMeanShiftFiltering(resized, cloned, 5, 20, 2);
				Imgproc.cvtColor(cloned, mRgba, Imgproc.COLOR_BGR2RGBA);
				mRgba.copyTo(output);
			}
			else{
				Imgproc.cvtColor(original, mRgba, Imgproc.COLOR_RGB2RGBA);
				mRgba.copyTo(output);
			}
		}
		else{
			Imgproc.cvtColor(original, mRgba, Imgproc.COLOR_RGB2RGBA);
			mRgba.copyTo(output);
		}
		AudioDemoActivityView.cache = original;
		//Imgproc.Canny(mGraySubmat, bwimg, 80, 100);
		//Imgproc.cvtColor(mGraySubmat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);		
		//if(block_size>0)
		//	Imgproc.adaptiveThreshold(mGraySubmat, bwimg, 128, 
		//			Imgproc.ADAPTIVE_THRESH_MEAN_C, 
		//			Imgproc.THRESH_BINARY_INV,
		//			block_size, delta);
        //else
        //	Imgproc.threshold(mGraySubmat, bwimg, 128, 
        //			255, Imgproc.THRESH_BINARY_INV);
		//Imgproc.cvtColor(bwimg, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);		
		
		
	}
}