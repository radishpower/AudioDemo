package org.opencv.samples.radishower;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
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
		
		//Imgproc.Canny(mGraySubmat, bwimg, 80, 100);
		//Imgproc.cvtColor(mGraySubmat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);		
		if(block_size>0)
			Imgproc.adaptiveThreshold(mGraySubmat, bwimg, 128, 
					Imgproc.ADAPTIVE_THRESH_MEAN_C, 
					Imgproc.THRESH_BINARY_INV,
					block_size, delta);
        else
        	Imgproc.threshold(mGraySubmat, bwimg, 128, 
        			255, Imgproc.THRESH_BINARY_INV);
		Imgproc.cvtColor(bwimg, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);		
		
		mRgba.copyTo(output);
	}
}