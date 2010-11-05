package com.cliqset.magicsig.encoding.test;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.cliqset.magicsig.encoding.Base64URLMagicSignatureEncoding;

public class Base64URLMagicSignatureEncodingTest {

	private static final String dataString = "salmon swim upstream.";
	private static byte[] dataBytes = null;
	private static final String encodedString = "c2FsbW9uIHN3aW0gdXBzdHJlYW0u";
	private static final byte[] encodedBytes = new byte[] { 99, 50, 70, 115, 98, 87, 57, 117, 73, 72, 78, 51, 97, 87, 48, 103, 100, 88, 66, 122, 100, 72, 74, 108, 89, 87, 48, 117 };
	
	static {
		try {
			dataBytes = dataString.getBytes("UTF-8"); 
		} catch (UnsupportedEncodingException uee) {}
	}
	
	@Test
	public void testEncodeToBytes() {
		try {
			Base64URLMagicSignatureEncoding encoder = new Base64URLMagicSignatureEncoding();
			byte[] encoded = encoder.encode(dataBytes);
			Assert.assertArrayEquals(encodedBytes, encoded);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testEncodeToString() {
		try {
			Base64URLMagicSignatureEncoding encoder = new Base64URLMagicSignatureEncoding();
			String encoded = encoder.encodeToString(dataBytes);
			Assert.assertEquals(encodedString, encoded);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDecodeString() {
		try {
			Base64URLMagicSignatureEncoding encoder = new Base64URLMagicSignatureEncoding();
			byte[] decoded = encoder.decode(encodedString);
			Assert.assertArrayEquals(dataBytes, decoded);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDecodeBytes() {
		try {
			Base64URLMagicSignatureEncoding encoder = new Base64URLMagicSignatureEncoding();
			byte[] decoded = encoder.decode(encodedBytes);
			Assert.assertArrayEquals(dataBytes, decoded);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}
