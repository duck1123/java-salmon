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
import java.security.Signature;
import java.security.SignatureException;

import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigException;

public class RSASHA256MagicSigAlgorithm implements MagicSigAlgorithm {

	private static final String IDENTIFIER = "RSA-SHA256";
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public byte[] sign(byte[] data, Key key) throws MagicSigException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		
		MagicKey magicKey = null;
		
		if (key instanceof MagicKey) {
			magicKey = (MagicKey)key;
			if (!"RSA".equals(magicKey.getType())) {
				throw new MagicSigException("Key must be a MagicKey of type RSA to use this algorithm.");	
			}
		} else {
			throw new MagicSigException("Key must be a MagicKey of type RSA to use this algorithm.");
		}
		
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(magicKey.getPrivateKey());
			sig.update(data);
			return sig.sign();
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSigException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSigException(ike);
		} catch (SignatureException se) {
			throw new MagicSigException(se);
		}
	}

	public boolean verify(byte[] data, byte[] signature, Key key) throws MagicSigException {
		if (null == data) { throw new IllegalArgumentException("data must not be null."); }
		if (null == key) { throw new IllegalArgumentException("key must not be null."); }
		if (null == signature) { throw new IllegalArgumentException("signature must not be null"); }
		
		Signature sig = null;
		
		MagicKey magicKey = null;
		
		if (key instanceof MagicKey) {
			magicKey = (MagicKey)key;
			if (!"RSA".equals(magicKey.getType())) {
				throw new MagicSigException("Key must be a MagicKey of type RSA to use this algorithm.");	
			}
		} else {
			throw new MagicSigException("Key must be a MagicKey of type RSA to use this algorithm.");
		}
		
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(magicKey.getPublicKey());
			sig.update(data);
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSigException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSigException("Key is invalid for this algorithm.", ike);
		} catch (SignatureException se) {
			throw new MagicSigException(se);
		}
	}
}
