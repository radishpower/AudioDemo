// Copyright (c) 2011 Szymon Jakubczak. All rights reserved.
// Use of this source code is governed by a license that can be found in
// the LICENSE file.
package org.opencv.samples.radishower;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SoundBox manages a bunch of sound clips. Load them up front, then tell the
 * SoudBox which clip to play, at what position and volume. The clip will loop
 * by default, but it can loop any smaller part of it too.
 * 
 * Each clip can only be played once at a time. To play the same sound multiple
 * times with overlap you need to load the sound bit as multiple clips.
 * 
 * Supports PCM in 8-bit, 16-bit, mono, stereo.
 * Support for sampling rates (and 8-bit) varies by device.
 * Samsung Galaxy S cracks at 48kHz.
 */
public class SoundBox {
	public static final String TAG = "SoundBox";
	
	class Track {
		final String filename;
		final AudioTrack track;
		final WaveFile wave;
		final int numSamples;
		Track(String filename) throws IOException {
			this.filename = filename;
			wave = new WaveFile(new FileInputStream(filename));
			// for memory consideration I highly recommend mono!
			track = new AudioTrack(AudioManager.STREAM_MUSIC, 
						wave.sampleRate, 
						wave.numChannels == 2 ? AudioFormat.CHANNEL_CONFIGURATION_STEREO 
											  : AudioFormat.CHANNEL_CONFIGURATION_MONO, 
						wave.bitsPerSample == 8 ? AudioFormat.ENCODING_PCM_8BIT 
								                : AudioFormat.ENCODING_PCM_16BIT, 
		                wave.data.length, AudioTrack.MODE_STATIC);
			// load the data into the track
			track.write(wave.data, 0, wave.data.length);
			// loop the whole track
			numSamples = wave.getLength();
			track.setLoopPoints(0, numSamples, -1);
		}
	}
	
	/**
	 * Array of the loaded clips.
	 * When AsyncLoad is executed it makes room in |clips| and stores the 
	 * track once it's fully loaded.
	 */
	private ArrayList<Track> clips = new ArrayList<Track>();

	
	/**
	 * load a clip from the given filename and store it
	 * Note: executes the task at creation time
	 * use .id to later refer to the loaded clip
	 */
	class AsyncLoad extends AsyncTask<String, Void, Track> {
		static final String TAG = "SoundBox.AsyncLoad";
		final int id;
		private final String filename;

		AsyncLoad(String filename) {
			this.id = clips.size();
			clips.add(null); // placeholder
			this.filename = filename;
			execute(); // auto-execute
		}
		@Override
		protected void onPostExecute(Track track) {
			if (track != null)
				clips.set(id, track);
			// else it remains null
		}
		@Override
		protected Track doInBackground(String... params) {
			Track track;
			try {
				track = new Track(filename);
			} catch(IOException e) {
				// bam!
				Log.e(TAG, String.format("Loading %s failed", filename), e);
				return null;
			}
			return track;
		}		
	}
	
	public SoundBox() {}
	
	/**
	 * Load asynchronously: at some point in the future the clip will be available.
	 * Rather than notifying anybody once the clip is available, we simply ignore
	 * all requests (below) if the clip is not there yet (or failed to load).
	 * @param filename
	 * @return clip id
	 */
	public int load(String filename) {
		return new AsyncLoad(filename).id;
	}
	
	public boolean isLoaded(int id) {
		return clips.get(id) != null;
	}
	
	// get rate (in samples/s)
	public int getRate(int id) {
		Track track = clips.get(id);
		if (track == null) return 0;
		return track.track.getSampleRate();
	}
	
	/**
	 * @param clip id
	 * @return length (in samples)
	 */
	public int getLength(int id) {
		Track track = clips.get(id);
		if (track == null) return 0;
		return track.numSamples;
	}
	
	// resume from current position
	public void play(int id) {
		Track track = clips.get(id);
		if (track == null) return;
		track.track.play();
	}
	
	/**
	 * seek to specific position
	 * @param clip id
	 * @param offset (in samples)
	 */
	public void seek(int id, int offset) {
		Track track = clips.get(id);
		if (track == null) return;
		AudioTrack atrack = track.track;
		int state = atrack.getPlayState();
		atrack.pause();
		atrack.setPlaybackHeadPosition(offset);
		// resume if necessary
		if (state == AudioTrack.PLAYSTATE_PLAYING)
			atrack.play();
	}
		
	public void pause(int id) {
		Track track = clips.get(id);
		if (track == null) return;
		track.track.pause();
	}
	
	// not sure what the difference between pause and stop
	// after stop it probably automatically seeks to 0
	public void stop(int id) {
		Track track = clips.get(id);
		if (track == null) return;
		track.track.stop();
	}

	/**
	 * volume normalized to 0-1
	 * @param id
	 * @param left
	 * @param right
	 */
	public void setVolume(int id, float left, float right) {
		Track track = clips.get(id);
		if (track == null) return;
		float maxvol = AudioTrack.getMaxVolume();
		track.track.setStereoVolume(left * maxvol, right * maxvol);
	}
}