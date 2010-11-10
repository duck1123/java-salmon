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
package com.cliqset.magicsig.algorithm.test;

import org.junit.Assert;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.SecretKey;
import com.cliqset.magicsig.algorithm.HMACSHA256MagicSigAlgorithm;

public class HMACSHA256MagicSignatureAlgorithmTest {

	private String stringData = "the salmon swim upstream.";
	private String hexKey = "501d2b82ac72f9dd74b00fe82976ae0ba0a0942c3a690f5d8350fa6e6485e1e0b789517702df9b14b79b781722faa0657fd839387ed9943527bd7a62d3e56234dad43d4bd4a078e9292d964789725d4e33dfef5b3c85c49bd713380c1065c2df10dbab7e3087ebe6e90afc87075129c99c79276a7287c22f279cae52a4340c35e33ddbc6f88a5369f679cf69b2030778290f76237bb84c896eda1ec9e1945ef1f63a6982a0f4eb0f36f5674a0a1962c2de68dacea90ff892a9aade123e834ed608c820508598c185d12149546988e59ee4984e0e1b5872e64e7dcad855f0933ed55cd575d421fcb721d4c02d2240bc34b94c34f4c80f739e6f54185f671d010f";
	private String hexSig = "92129940b5279864767db166de95bf05f405c443c0531c2d002b253e629f9c63";
	
	
	@Test
	public void testSign() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			byte[] sig = alg.sign(stringData.getBytes("UTF-8"), key);

			Assert.assertArrayEquals(Hex.decodeHex(hexSig.toCharArray()), sig);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifySuccess() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			Assert.assertTrue(alg.verify(stringData.getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), key));
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifyFailDataDifferent() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			Assert.assertFalse(alg.verify(stringData.substring(1).getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifyFailSigDifferent() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			Assert.assertFalse(alg.verify(stringData.getBytes("UTF-8"), Hex.decodeHex(hexSig.replace("c", "d").toCharArray()), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvalidKeyAlgorithm() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA1", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			alg.verify(stringData.substring(1).getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), key);
			Assert.fail();
		} catch (MagicSigException mse) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testExceptionVerifyNullKey() {
		try {
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			alg.verify(stringData.substring(1).getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), null);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testExceptionVerifyNullData() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			alg.verify(null, Hex.decodeHex(hexSig.toCharArray()), key);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testExceptionVerifyNullSig() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex(hexKey.toCharArray()));
			HMACSHA256MagicSigAlgorithm alg = new HMACSHA256MagicSigAlgorithm();
			alg.verify(stringData.getBytes("UTF-8"), null, key);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
