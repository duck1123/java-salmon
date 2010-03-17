package com.cliqset.salmon;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class MagicKey {

	private static final String SEPARATOR = ".";
	
	private String type;
	private byte[] n = null;
	private byte[] e = null;
	private byte[] d = null;
	
	public MagicKey() { }
	
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
