package org.opencv.samples.radishower;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.radishower.Process.Result;

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
		
		Mat resized = new Mat();
		Imgproc.resize(original, resized, new Size(original.width(), original.height()));
		
		Mat cloned = new Mat();
		Imgproc.pyrMeanShiftFiltering(resized, cloned, 5, 20, 2);
		Imgproc.cvtColor(cloned, mRgba, Imgproc.COLOR_BGR2RGBA);
		
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
		
		mRgba.copyTo(output);
	}
}