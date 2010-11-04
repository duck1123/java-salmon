package com.cliqset.magicsig.algorithm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;

import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicSignatureAlgorithm;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.SecretKey;

public class HMACSHA256MagicSignatureAlgorithm implements MagicSignatureAlgorithm {

	private static String IDENTIFIER = "HMAC-SHA256";
	
	private static final String algorithm = "HmacSHA256";
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public byte[] sign(byte[] data, Key key) throws MagicSignatureException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		
		SecretKey secretKey = null;
		if (key instanceof SecretKey) {
			secretKey = (SecretKey)key;
			if (!secretKey.getAlgorithm().equals(IDENTIFIER)) {
				throw new MagicSignatureException("Key must be a SecretKey suitable for algorithm " + IDENTIFIER + " to use this algorithm.");
			}
		} else {
			throw new MagicSignatureException("Key must be a SecretKey suitable for algorithm " + IDENTIFIER + " to use this algorithm.");
		}

		try {
			Mac mac = Mac.getInstance(algorithm);
			mac.init(secretKey.getSecretKey());
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSignatureException("Algorithm HmacSHA256 is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSignatureException("Key is invalid for this algorithm.", ike);
		}
		
	}

	public boolean verify(byte[] data, byte[] signature, Key key) throws MagicSignatureException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		if (null == signature) { throw new IllegalArgumentException("signature must not be null"); }
		
		byte[] computed = sign(data, key);
		return Arrays.equals(signature, computed);
	}

}
