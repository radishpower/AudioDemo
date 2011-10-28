// Copyright (c) 2011 Szymon Jakubczak. All rights reserved.
// Use of this source code is governed by a license that can be found in
// the LICENSE file.
package org.opencv.samples.radishower;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Utility to read little-endian integers from an InputStream. (DataInputStream
 * can only do big-endian.)
 */
class LittleEndianInputStream {
	private DataInputStream dis;
	private byte[] buf = new byte[8];

	LittleEndianInputStream(InputStream is) {
		dis = new DataInputStream(is);
	}

	void readFully(byte[] dst) throws IOException {
		dis.readFully(dst);
	}

	void readFully(byte[] dst, int offset, int byteCount) throws IOException {
		dis.readFully(dst, offset, byteCount);
	}

	int readByte() throws IOException {
		// these 0xff are necessary to convert from signed to unsigned (stupid
		// Java quirk)
		return dis.readByte() & 0xff;
	}

	int readShort() throws IOException {
		dis.readFully(buf, 0, 2);
		return (short) ((buf[0] & 0xff) | ((buf[1] & 0xff) << 8));
	}

	int readInt() throws IOException {
		dis.readFully(buf, 0, 4);
		return (buf[0] & 0xff) | ((buf[1] & 0xff) << 8)
				| ((buf[2] & 0xff) << 16) | ((buf[3] & 0xff) << 24);
	}
}

/**
 * Parser for WAVE file in PCM format.
 */
public class WaveFile {
	// magic chunk ids
	static final byte[] ID_RIFF = "RIFF".getBytes();
	static final byte[] ID_WAVE = "WAVE".getBytes();
	static final byte[] ID_FMT = "fmt ".getBytes();
	static final byte[] ID_DATA = "data".getBytes();

	/**
	 * Parser for one chunk of the WAVE format.
	 */
	class Chunk {
		byte[] id = new byte[4];
		int size;
		byte[] data;

		void read(InputStream is) throws IOException {
			LittleEndianInputStream lis = new LittleEndianInputStream(is);
			lis.readFully(id);
			size = lis.readInt();
			data = new byte[size];
			lis.readFully(data);
		}

		InputStream blob() {
			return new ByteArrayInputStream(data);
		}
	}

	public final int numChannels;
	public final int sampleRate;
	public final int bitsPerSample;
	public final byte[] data;

	public WaveFile(InputStream is) throws IOException {
		Chunk riff = new Chunk();
		riff.read(is);
		check(Arrays.equals(ID_RIFF, riff.id), "Bad header");
		is = riff.blob();
		byte[] format = new byte[4];
		int nr = is.read(format);
		check(nr == 4, "Missing format");
		check(Arrays.equals(ID_WAVE, format), "Bad format");
		Chunk fmt = new Chunk();
		fmt.read(is);
		check(Arrays.equals(ID_FMT, fmt.id), "Missing fmt");
		Chunk datachunk = new Chunk();
		datachunk.read(is);
		check(Arrays.equals(ID_DATA, datachunk.id), "Missing data");

		LittleEndianInputStream lis = new LittleEndianInputStream(fmt.blob());
		int audioFormat = lis.readShort();
		check(audioFormat == 1, "Only PCM files supported");
		numChannels = lis.readShort();
		sampleRate = lis.readInt();
		int byteRate = lis.readInt();
		int blockAlign = lis.readShort();
		bitsPerSample = lis.readShort();
		check(byteRate == sampleRate * numChannels * bitsPerSample / 8,
				"Byte rate mismatch");
		check(blockAlign == numChannels * bitsPerSample / 8,
				"Block align mismatch");

		data = datachunk.data;
	}

	private static void check(boolean val, String msg) throws IOException {
		if (!val) {
			throw new IOException(msg);
		}
	}

	/**
	 * @return file length in samples
	 */
	public int getLength() {
		return data.length * 8 / (numChannels * bitsPerSample);
	}

	/**
	 * @return approx. file length in milliseconds
	 */
	public int getTimeLength() {
		return (getLength() * 1000) / sampleRate;
	}
}