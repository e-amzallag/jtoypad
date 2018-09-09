/*
 * Copyright 2018 Erik Amzallag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dajlab.jtoypad.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Uitlity class for decoding a tag.
 * 
 * @author Erik Amzallag
 *
 */
public class TagDecoder {

	/**
	 * Logger.
	 */
	private static Logger logger = LogManager.getLogger(TagDecoder.class);

	/**
	 * Characters resource.
	 */
	private static ResourceBundle characters = PropertyResourceBundle.getBundle("characters");
	/**
	 * Vehicles resource.
	 */
	private static ResourceBundle vehicles = PropertyResourceBundle.getBundle("vehicles");

	/**
	 * Key for TEA (see https://en.wikipedia.org/wiki/Tiny_Encryption_Algorithm
	 * for more about TEA).
	 */
	private static byte[] teakey = { 0x55, (byte) 0xFE, (byte) 0xF6, (byte) 0xB0, 0x62, (byte) 0xBF, 0x0B, 0x41,
			(byte) 0xC9, (byte) 0xB3, 0x7C, (byte) 0xB4, (byte) 0x97, 0x3E, 0x29, 0x7B };

	/**
	 * Encipher v0 and v1.
	 * 
	 * @param v0
	 *            v0
	 * @param v1
	 *            v1
	 * @return encrypted data
	 */
	private static int[] encipher(final int v0, final int v1) {
		int y = v0;
		int z = v1;
		int sum = 0;
		final int delta = 0x9E3779B9;
		final int a = readUInt32LE(teakey, 0);
		final int b = readUInt32LE(teakey, 4);
		final int c = readUInt32LE(teakey, 8);
		final int d = readUInt32LE(teakey, 12);
		int n = 32;

		while (n-- > 0) {
			sum += delta;
			y += (z << 4) + a ^ z + sum ^ (z >>> 5) + b;
			z += (y << 4) + c ^ y + sum ^ (y >>> 5) + d;
		}
		return new int[] { y, z };
	}

	/**
	 * Decipher v0 and v1
	 * 
	 * @param v0
	 *            v0
	 * @param v1
	 *            v1
	 * @return decrypted data
	 */
	private static int[] decipher(final int v0, final int v1) {

		int y = v0;
		int z = v1;
		int sum = 0xC6EF3720;
		final int delta = 0x9E3779B9;
		final int a = readUInt32LE(teakey, 0);
		final int b = readUInt32LE(teakey, 4);
		final int c = readUInt32LE(teakey, 8);
		final int d = readUInt32LE(teakey, 12);
		int n = 32;

		while (n-- > 0) {
			z -= (y << 4) + c ^ y + sum ^ (y >>> 5) + d;
			y -= (z << 4) + a ^ z + sum ^ (z >>> 5) + b;
			sum -= delta;
		}

		return new int[] { y, z };
	}

	/**
	 * Encode data.
	 * 
	 * @param data
	 *            data
	 * @return encoded data
	 */
	public static byte[] encode(final byte data[]) {

		logger.trace("Encoding {}", byteToHex(data));

		byte[] data2 = ByteBuffer.allocate(8).putInt((int) readUInt32LE(data, 0)).putInt((int) readUInt32LE(data, 4))
				.array();

		byte bp[] = data2;
		int r0 = bp[0] << 24 | (bp[1] & 0xff) << 16 | (bp[2] & 0xff) << 8 | bp[3] & 0xff;
		int r1 = bp[4] << 24 | (bp[5] & 0xff) << 16 | (bp[6] & 0xff) << 8 | bp[7] & 0xff;
		final int r[] = encipher(r0, r1);

		ByteBuffer bff = ByteBuffer.allocate(8);
		writeUInt32LE(r[0], bff);
		writeUInt32LE(r[1], bff);

		return bff.array();
	}

	/**
	 * Decode data
	 * 
	 * @param data
	 *            data
	 * @return decoded data
	 */
	private static byte[] decodeData(final byte[] data) {

		int d1 = readUInt32LE(data, 0);
		int d2 = readUInt32LE(data, 4);

		final int tmp[] = decipher(d1, d2);

		ByteBuffer bff = ByteBuffer.allocate(8);
		writeUInt32LE(tmp[0], bff);
		writeUInt32LE(tmp[1], bff);

		return bff.array();
	}

	/**
	 * Decode tag.
	 * 
	 * @param data
	 *            data
	 * @return a Tag with id and name (if a character's name matches the id)
	 */
	public static Tag decode(final byte[] data) {

		logger.trace("Decoding {}", byteToHex(data));

		Tag tag = new Tag();

		byte[] dataDecod = decodeData(data);

		int id = readUInt32LE(dataDecod, 0);

		tag.setId(id);
		logger.debug("Character's id = [{}]", id);
		String name = null;
		try {
			name = characters.getString(String.valueOf(id));
			logger.debug("Character's name = [{}]", name);
		} catch (MissingResourceException e) {
			logger.debug("No character for id [{}]", id);
		}
		tag.setName(name);

		return tag;

	}

	/**
	 * Read int 32 bits Little Endian
	 * 
	 * @param bytes
	 *            bytes
	 * @param pointer
	 *            pointer
	 * @return int
	 */
	private static int readUInt32LE(byte[] bytes, int pointer) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes, pointer, 4);
		buffer.put(new byte[4]);
		buffer.flip();
		return buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	/**
	 * Write a value int 32 bits Little Endian to the buffer.
	 * 
	 * @param v
	 *            value
	 * @param buf
	 *            buffer
	 */
	private static void writeUInt32LE(int v, ByteBuffer buf) {
		buf.put((byte) (v & 0x000000ff));
		buf.put((byte) ((v & 0x0000ff00) >> 8));
		buf.put((byte) ((v & 0x00ff0000) >> 16));
		buf.put((byte) ((v & 0xff000000) >> 24));
	}

	/**
	 * Format a byte array to a String for debug purpose.
	 * 
	 * @param data
	 *            data
	 * @return a formatted string
	 */
	public final static String byteToHex(byte[] data) {

		StringBuilder mess = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			byte b = data[i];
			mess.append(String.format("%02X ", b));
		}
		mess.append(String.format("%02X", data[data.length - 1]));
		return mess.toString();
	}

}
