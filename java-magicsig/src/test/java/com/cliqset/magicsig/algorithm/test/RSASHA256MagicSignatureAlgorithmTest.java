package com.cliqset.magicsig.algorithm.test;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.SecretKey;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;

public class RSASHA256MagicSignatureAlgorithmTest {
	
	private String stringData = "the salmon swim upstream.";
	private String magicKey = "RSA.mVgY8RN6URBTstndvmUUPb4UZTdwvwmddSKE5z_jvKUEK6yk1u3rrC9yN8k6FilGj9K0eeUPe2hf4Pj-5CmHww==.AQAB.Lgy_yL3hsLBngkFdDw1Jy9TmSRMiH6yihYetQ8jy-jZXdsZXd8V5ub3kuBHHk4M39i3TduIkcrjcsiWQb77D8Q==";
	private String hexSig = "2a0d51c9604d410a8710ab8985535ea2c2b2a166efdd684bf3496555107f171ca7cfb8e5c3695927e70dfa27fb81d6b5d807457c9fd80d22bcfa0c7d48572aac";
	
	
	@Test
	public void testSign() {
		try {
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
			byte[] sig = alg.sign(stringData.getBytes("UTF-8"), key);

			Assert.assertArrayEquals(Hex.decodeHex(hexSig.toCharArray()), sig);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifySuccess() {
		try {
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
			Assert.assertTrue(alg.verify(stringData.getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifyFailDataDifferent() {
		try {
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
			Assert.assertFalse(alg.verify(stringData.substring(1).getBytes("UTF-8"), Hex.decodeHex(hexSig.toCharArray()), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testVerifyFailSigDifferent() {
		try {
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
			Assert.assertFalse(alg.verify(stringData.getBytes("UTF-8"), Hex.decodeHex(hexSig.replace("c", "d").toCharArray()), key));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvalidKeyAlgorithm() {
		try {
			SecretKey key = new SecretKey("HMAC-SHA256", Hex.decodeHex("1234567890abcdef".toCharArray()));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
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
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
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
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
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
			MagicKey key = new MagicKey(magicKey.getBytes("ASCII"));
			RSASHA256MagicSigAlgorithm alg = new RSASHA256MagicSigAlgorithm();
			alg.verify(stringData.getBytes("UTF-8"), null, key);
			Assert.fail();
		} catch (IllegalArgumentException iae) {
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}