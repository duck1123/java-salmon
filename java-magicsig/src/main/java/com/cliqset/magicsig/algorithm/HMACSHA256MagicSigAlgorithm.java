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
