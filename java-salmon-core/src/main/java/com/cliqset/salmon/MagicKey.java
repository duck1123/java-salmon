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
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.xml.crypto.dsig.DigestMethod;

public class MagicKey {

	private static final String SEPARATOR = ".";
	
	private String type;
	private byte[] n = null;
	private byte[] e = null;
	private byte[] d = null;
	
	public MagicKey() { }
	
	public MagicKey(String type, String armoredN, String armoredE) {
		if (type == null) { throw new IllegalArgumentException("type must not be null"); }
		if (armoredN == null) { throw new IllegalArgumentException("N must not be null"); }
		if (armoredE == null) { throw new IllegalArgumentException("E must not be null"); }
		this.setType(type);
		this.setN(MagicSigUtil.decode(armoredN));
		this.setE(MagicSigUtil.decode(armoredE));
	}
	
	public MagicKey(String type, String armoredN, String armoredE, String armoredD) {
		if (type == null) { throw new IllegalArgumentException("type must not be null"); }
		if (armoredN == null) { throw new IllegalArgumentException("N must not be null"); }
		if (armoredE == null) { throw new IllegalArgumentException("E must not be null"); }
		if (armoredD == null) { throw new IllegalArgumentException("D must not be null"); }
		this.setType(type);
		this.setN(MagicSigUtil.decode(armoredN));
		this.setE(MagicSigUtil.decode(armoredE));
		this.setD(MagicSigUtil.decode(armoredD));
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
			setN(MagicSigUtil.decode(split[1].getBytes("ASCII")));
			setE(MagicSigUtil.decode(split[2].getBytes("ASCII")));
			
			if (split.length > 3) {
				setD(MagicSigUtil.decode(split[3].getBytes("ASCII")));
			}
		} catch (UnsupportedEncodingException e) { }
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

	public String getKeyhash() {
		byte[] hash = null;
		 try { 
			 MessageDigest md = MessageDigest.getInstance("SHA-256");
			 hash = md.digest(this.toString().getBytes("ASCII"));
		 } catch (NoSuchAlgorithmException nsae) {
			 //TODO: what do we do with these?  are they ever going to occur?
			 return null;
		 } catch (UnsupportedEncodingException uee) {
			//TODO: what do we do with these?  are they ever going to occur?
			 return null;
		 }
		 return MagicSigUtil.encodeToString(hash);
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
		sb.append(MagicSigUtil.encodeToString(n));
		sb.append(SEPARATOR);
		sb.append(MagicSigUtil.encodeToString(e));
		if (includePrivate && null != this.d) {
			sb.append(SEPARATOR);
			sb.append(MagicSigUtil.encodeToString(d));
		}
		return sb.toString();
	}
}
