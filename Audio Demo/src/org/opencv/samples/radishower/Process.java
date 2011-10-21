package org.opencv.samples.radishower;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Process {
	private Mat localYUV = new Mat();
	private Mat mRgba = new Mat();
	private Mat mGraySubmat = new Mat();
	private Mat bwimg = new Mat();
	private List<Point> corners = new ArrayList<Point>();
	private int headervalue = 200;
	private int[] results = new int[8];
	
	public static class Result {
		int value;
		double threshold;
		Result(int v, double t) {
			value = v;
			threshold = t;
		}
	}
	
	public Result process(Mat mYuv, int width, int height, Mat output) {
		Result result = null;
		
		corners.clear();
		for (int i=0; i<4; i++) {
			corners.add(new Point(0,0));
		}
		
		localYUV = mYuv.clone();
		mGraySubmat = mYuv.submat(0, width, 0, height);
		List<Mat> contours = new ArrayList<Mat>();
		Mat unused = new Mat();
		
		Imgproc.Canny(mGraySubmat, bwimg, 80, 100);
		mGraySubmat = bwimg.clone();
		Imgproc.findContours(mGraySubmat, contours, unused, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat approxCurve = new Mat();
		Imgproc.cvtColor(bwimg, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
		int count = 0;
		for (int i=0; i<contours.size(); i++) {
			double tmpval = Imgproc.contourArea(contours.get(i));
			if (tmpval > 200) {
				Imgproc.approxPolyDP(contours.get(i), approxCurve, tmpval*0.0001, true);
				if (approxCurve.total() == 4) {
					count = count + 1;
					//Imgproc.drawContours(mRgba, contours, i, new Scalar(255, 0, 0, 255));
					//Make sure that the smallest coordinate gets added to upper left
					int tl = 0; int bl = 0; int br = 0; int tr = 0; 
					int tmpmin = (int) (approxCurve.get(0, 0)[0]+approxCurve.get(0, 0)[1]);
					int tmpmax = (int) (approxCurve.get(0, 0)[0]+approxCurve.get(0, 0)[1]);
					for (int j=1; j<4; j++) {
						if (((int) (approxCurve.get(j, 0)[0]+approxCurve.get(j, 0)[1])) < tmpmin) {
							tl = j;
							tmpmin = ((int) (approxCurve.get(j, 0)[0]+approxCurve.get(j, 0)[1]));
						}
						if (((int) (approxCurve.get(j, 0)[0]+approxCurve.get(j, 0)[1])) > tmpmax) {
							br = j;
							tmpmax = ((int) (approxCurve.get(j, 0)[0]+approxCurve.get(j, 0)[1]));
						}
					}
					tmpmin = (int) (approxCurve.get(((tl+1)%4), 0)[1]-approxCurve.get((tl), 0)[1]);
					tmpmax = (int) (approxCurve.get(((tl+7)%4), 0)[1]-approxCurve.get((tl), 0)[1]);
					if (tmpmin > tmpmax) {
					     tr = (tl+1)%4; bl = (tl+3)%4;
					}
					else {
						tr = (tl+7)%4; bl=(tl+9)%4;
					}
					
					corners.set(0, new Point(approxCurve.get(tl,0)[0], approxCurve.get(tl, 0)[1]));
					corners.set(1, new Point(approxCurve.get(bl,0)[0], approxCurve.get(bl, 0)[1]));
					corners.set(2, new Point(approxCurve.get(br,0)[0], approxCurve.get(br, 0)[1]));
					corners.set(3, new Point(approxCurve.get(tr,0)[0], approxCurve.get(tr, 0)[1]));
					
					Imgproc.cvtColor(localYUV, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
					int diam_size = 5;
					List<Point> centerpoints = samplegrid(width, height, 0, diam_size);
					result = getOneByte(perspective_transform(mRgba, width, height), centerpoints, diam_size);
					if (result.value == headervalue) {
						//for (int ii=0; ii<8; i++) {
						//	centerpoints = samplegrid(width, height, (int) (0.11111/2+0.11111*ii)*height, diam_size);
						//	results[ii] = getOneByte(perspective_transform(mRgba, width, height), centerpoints, diam_size).value;
						//}
						break;
					}
					else {
						result = null;
					}
				}
			}
		}
		Imgproc.cvtColor(localYUV, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
		if (result != null) {
			Core.fillConvexPoly(mRgba, corners, new Scalar(0, 0, 128, 128));
			Core.putText(mRgba, "S: " + Integer.toString(result.value), new Point(10,200), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
			//Core.putText(mRgba, "S: " + Integer.toString(count), new Point(0,100), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, new Scalar(255, 0, 0, 255), 2);
			for (int i=0; i<8; i++) {
				Core.putText(mRgba, Integer.toString(count)+" ", new Point(i*60,100), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, new Scalar(255, 0, 0, 255), 2);
			}
		}
		mRgba.copyTo(output);
		return result;
	}
	
	 // Takes the corners and remaps it to a rectangular figure
    public Mat perspective_transform(Mat distorted, int width, int height) {
            Mat transformMatrix = new Mat();
            Mat result = new Mat();
            List<Point> newcorners = new ArrayList<Point>();
            newcorners.add(new Point(0,0));
            newcorners.add(new Point(height,0));
            newcorners.add(new Point(height,width));
            newcorners.add(new Point(0,width));

            transformMatrix = Imgproc.getPerspectiveTransform(corners, newcorners);
            Imgproc.warpPerspective(distorted, result, transformMatrix, distorted.size());

            return result;
    }
    
    public Result getOneByte(Mat undistorted, List<Point> centerpoints, int samplediam) {
    	Mat mHsv = new Mat();
    	int numbits = centerpoints.size();
    	numbits = 9;
    	
		Imgproc.cvtColor(undistorted, mHsv, Imgproc.COLOR_RGB2HSV); //optimize this later so that we don't have to recolor the whole thing	
    
		double[] divsum = new double[numbits];

		for (int i=0; i<(numbits-1); i++) {
			divsum[i]=0;
    		for (int y=0; y<samplediam; y++) {
    			for (int x=0; x<samplediam; x++) {
    				double s = mHsv.get((int) Math.floor(centerpoints.get(i).y+y), 
    						            (int) Math.floor(centerpoints.get(i).x+x))[1];
    				double sf = mHsv.get((int) Math.floor(centerpoints.get(i+1).y+y), 
    						             (int) Math.floor(centerpoints.get(i+1).x+x))[1];
    				
    				divsum[i] = Math.abs(s-sf) + divsum[i];
    			}
    		}    		
		}
		
		double threshold = 0;
		for (int i=0; i<(numbits-1); i++) {
			threshold = threshold + divsum[i];
		}
		threshold = threshold/numbits;
		
		int byteval = 0;
		for (int i=0; i<(numbits-1); i++) {
    		//Core.putText(undistorted, Double.toString(divsum[i]), centerpoints.get(i), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, new Scalar(255, 0, 0, 255), 3);
    		
    		if (divsum[i] > threshold) {
    			byteval = byteval + 1;
    		}
    		else {
    			byteval = byteval + 0;
    		}
    		if (i!=(numbits-2)) byteval = byteval << 1;
		}
		//Core.putText(undistorted, String.format("%.2f", threshold), new Point(200,100), 3/* CV_FONT_HERSHEY_COMPLEX */, 1, new Scalar(255, 0, 0, 255), 3);
		//Core.putText(undistorted, "S: " + Integer.toString(byteval), new Point(400,100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
		
		return new Result(byteval, threshold);
		
    }
    
    // samples, reads mRgba
    public List<Point> samplegrid(int width, int height, int ystart, int samplediam) {
		int numbits = 9; //We'll soon put in a byte interfaces essentially its 8+1
		List<Point> centerpoints = new ArrayList<Point>();
		int divx = height/numbits;
		//int ystart = (int) Math.floor(0.01*(double)(width)); //Supposedly the code is of 2.5% heights
		
		for (int i=0; i<numbits; i++) centerpoints.add(new Point(divx/2+i*divx-samplediam/2, ystart));
		//for (int i=0; i<numbits; i++) centerpoints.add(new Point(ystart,divx/2+i*divx-samplediam/2));
		return centerpoints;
    }
}