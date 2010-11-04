package com.cliqset.magicsig;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

public class SecretKey extends Key {

	protected static Map<String, String> spec2JavaAlgorithmMap = new HashMap<String, String>(); 
	
	static {
		spec2JavaAlgorithmMap.put("HMAC-SHA256", "HmacSHA256");
	}
	
	private String algorithm;
	
	private byte[] bytes;
	
	public SecretKey(String algorithm, byte[] bytes) throws MagicSignatureException {
		if (null == algorithm) { throw new IllegalArgumentException("algorithm must not be null."); }
		if (null == bytes || bytes.length < 1) { throw new IllegalArgumentException("bytes must contain a valid key value."); }		
		if (null == spec2JavaAlgorithmMap.get(algorithm)) { throw new MagicSignatureException("Unknown algorithm: " + algorithm); }
		
		this.algorithm = algorithm;
		this.bytes = bytes;
	}
	
	@Override
	public boolean supportsKeyId() {
		return false;
	}
	
	@Override
	public String getKeyId() {
		return null;
	}
	
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	public SecretKeySpec getSecretKey() {
		return new SecretKeySpec(bytes, algorithm);
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean includePrivate) {
		if (includePrivate) {
			return algorithm + ":" + bytes.toString();
		} else {
			return algorithm + ":" + bytes.length + " crazy bytes";
		}
	}
}
