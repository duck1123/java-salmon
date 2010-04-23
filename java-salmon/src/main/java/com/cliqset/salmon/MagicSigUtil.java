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

package com.cliqset.salmon;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MagicSigUtil {

	public static final String ALGORITHM = "RSA-SHA256";
	public static final String ENCODING = "base64url";
	
	public static String encodeToString(byte[] data) {
		String b64 = Base64.encodeBase64URLSafeString(data);
		//looks like encodeBase64URLSafeString doesn't pad to / 4
		while (b64.length() % 4 != 0) {
			b64 += (char)61;
		}
		return b64;
	}
	
	public static byte[] encode(byte[] data) {
		try {
			return encodeToString(data).getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static byte[] decode(String armoredData) {
		//remove whitespace
		armoredData = armoredData.replace("\\s", "");
		
		//pad so length is divisible by 4
		while (armoredData.length() % 4 != 0) {
			armoredData += (char)61;
		}

		return Base64.decodeBase64(armoredData);
	}
	
	public static byte[] decode(byte[] armoredDataBytes) {
		String armoredData = null;
		try {
			armoredData = new String(armoredDataBytes, "ASCII");
		} catch (Exception e) {}
		return decode(armoredData);
	}
	
	public static boolean verify(byte[] data, byte[] signature, MagicKey key) throws SalmonException {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(key.getPublicKey());
			sig.update(data);
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException nsae) {
			throw new SalmonException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new SalmonException(ike);
		} catch (SignatureException se) {
			throw new SalmonException(se);
		}
	}
	
	public static byte[] sign(byte[] data, MagicKey key) throws SalmonException {
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(key.getPrivateKey());
			sig.update(data);
			return sig.sign();
		} catch (NoSuchAlgorithmException nsae) {
			throw new SalmonException("Signature Algorithm SHA256withRSA is required", nsae);
		} catch (InvalidKeyException ike) {
			throw new SalmonException(ike);
		} catch (SignatureException se) {
			throw new SalmonException(se);
		}
	}
}
