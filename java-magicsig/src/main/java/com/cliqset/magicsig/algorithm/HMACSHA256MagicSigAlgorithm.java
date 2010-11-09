package com.cliqset.magicsig.algorithm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;

import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.SecretKey;

public class HMACSHA256MagicSigAlgorithm implements MagicSigAlgorithm {

	public static String ALGORITHM_IDENTIFIER = "HMAC-SHA256";
	
	private static final String algorithm = "HmacSHA256";

	public byte[] sign(byte[] data, Key key) throws MagicSigException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		
		SecretKey secretKey = null;
		if (key instanceof SecretKey) {
			secretKey = (SecretKey)key;
			if (!secretKey.getAlgorithm().equals(ALGORITHM_IDENTIFIER)) {
				throw new MagicSigException("Key must be a SecretKey suitable for algorithm " + ALGORITHM_IDENTIFIER + " to use this algorithm.");
			}
		} else {
			throw new MagicSigException("Key must be a SecretKey suitable for algorithm " + ALGORITHM_IDENTIFIER + " to use this algorithm.");
		}

		try {
			Mac mac = Mac.getInstance(algorithm);
			mac.init(secretKey.getSecretKey());
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSigException("Algorithm HmacSHA256 is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSigException("Key is invalid for this algorithm.", ike);
		}
		
	}

	public boolean verify(byte[] data, byte[] signature, Key key) throws MagicSigException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		if (null == signature) { throw new IllegalArgumentException("signature must not be null"); }
		
		byte[] computed = sign(data, key);
		return Arrays.equals(signature, computed);
	}

}
