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

package com.cliqset.magicsig;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.codec.binary.Base64;
public class MagicKey {

	private static final String SEPARATOR = ".";
	
	private String type;
	private byte[] n = null;
	private byte[] e = null;
	private byte[] d = null;
	private String keyId = null;
	
	public MagicKey() { }
	
	public MagicKey(String type, String armoredN, String armoredE) {
		if (type == null) { throw new IllegalArgumentException("type must not be null"); }
		if (armoredN == null) { throw new IllegalArgumentException("N must not be null"); }
		if (armoredE == null) { throw new IllegalArgumentException("E must not be null"); }
		this.setType(type);
		this.setN(Base64.decodeBase64(armoredN));
		this.setE(Base64.decodeBase64(armoredE));
	}
	
	public MagicKey(String type, String armoredN, String armoredE, String armoredD) {
		if (type == null) { throw new IllegalArgumentException("type must not be null"); }
		if (armoredN == null) { throw new IllegalArgumentException("N must not be null"); }
		if (armoredE == null) { throw new IllegalArgumentException("E must not be null"); }
		if (armoredD == null) { throw new IllegalArgumentException("D must not be null"); }
		this.setType(type);
		this.setN(Base64.decodeBase64(armoredN));
		this.setE(Base64.decodeBase64(armoredE));
		this.setD(Base64.decodeBase64(armoredD));
	}
	
	public MagicKey(byte[] magicKey) {
		String mkString = null;
		try {
			mkString = new String(magicKey, "ASCII");
		} catch (UnsupportedEncodingException e) {}
		
		String[] split = mkString.split("\\.");
		if (split.length < 3) {
			throw new IllegalArgumentException("Magic Key must have at least 3 segments.");
		}
		setType(split[0]);
		
		try {
			setN(Base64.decodeBase64(split[1].getBytes("ASCII")));
			setE(Base64.decodeBase64(split[2].getBytes("ASCII")));
			
			if (split.length > 3) {
				setD(Base64.decodeBase64(split[3].getBytes("ASCII")));
			}
		} catch (UnsupportedEncodingException e) { }
	}
	
	public MagicKey withN(String value) {
		this.n = Base64.decodeBase64(value);
		return this;
	}
	
	public MagicKey withE(String value) {
		this.e = Base64.decodeBase64(value);
		return this;
	}
	
	public MagicKey withD(String value) {
		this.d = Base64.decodeBase64(value);
		return this;
	}
	
	public MagicKey withType(String value) {
		this.type = value;
		return this;
	}
	
	public MagicKey withKeyId(String value) {
		this.keyId = value;
		return this;
	}
	
	public PrivateKey getPrivateKey() {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(new RSAPrivateKeySpec(new BigInteger(1, getN()), new BigInteger(1, getD())));
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (InvalidKeySpecException ex) {
			return null;
		}
	}
	
	public PublicKey getPublicKey() {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(1, getN()), new BigInteger(1, getE())));
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (InvalidKeySpecException ex) {
			return null;
		}
	}

	public String getKeyId() {
		return this.keyId;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public boolean hasPrivateKey() {
		return null != this.getD();
	}

	public void setN(byte[] n) {
		this.n = n;
	}

	public byte[] getN() {
		return n;
	}

	public void setE(byte[] e) {
		this.e = e;
	}

	public byte[] getE() {
		return e;
	}

	public void setD(byte[] d) {
		this.d = d;
	}

	public byte[] getD() {
		return d;
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean includePrivate) {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(SEPARATOR);
		sb.append(Base64.encodeBase64URLSafeString(n));
		sb.append(SEPARATOR);
		sb.append(Base64.encodeBase64URLSafeString(e));
		if (includePrivate && null != this.d) {
			sb.append(SEPARATOR);
			sb.append(Base64.encodeBase64URLSafeString(d));
		}
		return sb.toString();
	}
}
