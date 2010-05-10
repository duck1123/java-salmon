package com.cliqset.salmon.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class BaseTestCase {
	
	public static byte[] getBytes(String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = BaseTestCase.class.getResourceAsStream(filename);
		try {
			int i;
			while (-1 != (i = data.read(buffer, 0, 512))) {
				baos.write(buffer, 0, i);
			}
		} catch (IOException e) {
			
		}
		return baos.toByteArray();
	}
	
	public static String getString(String filename, String encoding) {
		try {
			return new String(getBytes(filename), encoding);
		} catch (Exception e) {
			return "";
		}
	}
}
