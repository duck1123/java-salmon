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

import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSignatureAlgorithm;
import com.cliqset.magicsig.MagicSignatureException;

public class RSASHA256MagicSignatureAlgorithm implements MagicSignatureAlgorithm {

	private static final String IDENTIFIER = "RSA-SHA256";
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public byte[] sign(byte[] data, MagicKey key) throws MagicSignatureException {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(key.getPrivateKey());
			sig.update(data);
			return sig.sign();
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSignatureException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSignatureException(ike);
		} catch (SignatureException se) {
			throw new MagicSignatureException(se);
		}
	}

	public boolean verify(byte[] data, byte[] signature, MagicKey key) throws MagicSignatureException {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(key.getPublicKey());
			sig.update(data);
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException nsae) {
			throw new MagicSignatureException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new MagicSignatureException(ike);
		} catch (SignatureException se) {
			throw new MagicSignatureException(se);
		}
	}
}
