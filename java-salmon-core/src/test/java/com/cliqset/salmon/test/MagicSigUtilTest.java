/*
	Copyright 2010 Cliqset Inc.
	
	Licensed under the Apache License, Version 2.0 (the "License"); 
	you may not use this file except in compliance with the License. 
	You may obtain a copy of the License at 
	
		http://www.apache.org/licenses/LICENSE-2.0 
	
	Unless required by applicable law or agreed to in writing, software 
	distributed under the License is distributed on an "AS IS" BASIS, 
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
	See the License for the specific language governing permissions and 
	limitations under the License.
*/

package com.cliqset.salmon.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cliqset.salmon.MagicKey;
import com.cliqset.salmon.MagicSigUtil;

import org.junit.Assert;
import org.junit.Test;

public class MagicSigUtilTest {

	@Test
	public void testEncode() {
		String encoded = MagicSigUtil.encodeToString(getBytes("/SpecSampleEntry.xml"));
		String expected = getString("/SpecSampleData.txt", "ASCII");
		Assert.assertEquals(expected, encoded);
	}
	
	@Test
	public void testEncodePony() {
		String encoded = MagicSigUtil.encodeToString(getBytes("/PonyString.txt"));
		String expected = getString("/PonyEncodedString.txt", "ASCII");
		Assert.assertEquals(expected, encoded);
	}
	
	@Test
	public void testDecode() {
		byte[] decoded = MagicSigUtil.decode(getBytes("/SpecSampleData.txt"));
		
		byte[] expected = getBytes("/SpecSampleEntry.xml");
		
		Assert.assertArrayEquals(expected, decoded);
	}

	@Test
	public void testDecode2() {
		byte[] decoded = MagicSigUtil.decode("ALiV7JeHYt08h3QLfZc-AFEuXUzWSelfgsDfalO7hwfUvbVsMOt31dALiXpk_UEtjIYAAyBV2Qis0QEIrVd9JhazCohxOeLfQ0s0FGLI61WzT5jeHqxRGekvTCjabNK5l3q-rnh0W4bMDE98zTLs8V547-YHFcg6LA39JyVztTMJ");
		
		Assert.assertEquals(129, decoded.length);
	}

	@Test
	public void testEncodeToStringDecode() {
		byte[] entry = getBytes("/entry.txt");
		String encoded = MagicSigUtil.encodeToString(entry);
		System.out.println(encoded);
		byte[] decoded = MagicSigUtil.decode(encoded);
		try {
			System.out.println(new String(decoded, "UTF-8"));
		} catch (Exception e) {}
		Assert.assertArrayEquals(entry, decoded);
	}
	
	@Test
	public void testSignEncodeDecodeVerify() {
		try{
			byte[] original = getBytes("/DemoEntry.txt");
			MagicKey key = new MagicKey(getBytes("/DemoKeys.txt"));
			String encodedSig = MagicSigUtil.encodeToString(MagicSigUtil.sign(MagicSigUtil.encode(original), key));
			System.out.println(encodedSig);
			Assert.assertTrue(MagicSigUtil.verify(MagicSigUtil.encode(original), MagicSigUtil.decode(encodedSig), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSignEncodeDecodeVerifyPony() {
		try{
			byte[] original = getBytes("/PonyString.txt");
			MagicKey key = new MagicKey(getBytes("/PonyKey.txt"));
			String encodedSig = MagicSigUtil.encodeToString(MagicSigUtil.sign(MagicSigUtil.encode(original), key));
			System.out.println(encodedSig);
			Assert.assertTrue(MagicSigUtil.verify(MagicSigUtil.encode(original), MagicSigUtil.decode(encodedSig), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	private byte[] getBytes(String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = MagicSigUtilTest.class.getResourceAsStream(filename);
		try {
			int i;
			while (-1 != (i = data.read(buffer, 0, 512))) {
				baos.write(buffer, 0, i);
			}
		} catch (IOException e) {
			
		}
		return baos.toByteArray();
	}
	
	private String getString(String filename, String encoding) {
		try {
			return new String(getBytes(filename), encoding);
		} catch (Exception e) {
			return "";
		}
	}
}
