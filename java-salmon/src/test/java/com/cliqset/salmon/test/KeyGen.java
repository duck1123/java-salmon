package com.cliqset.salmon.test;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import com.cliqset.salmon.MagicSigUtil;

public class KeyGen {

	private static final int keySize = 512;
	private static final String fileName = "/magickey.txt";
	
	public static void main(String[] args) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write("RSA.".getBytes("ASCII"));
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		    keyPairGenerator.initialize(keySize);
		    KeyPair keyPair = keyPairGenerator.genKeyPair();
	
		    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		    RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
		    RSAPrivateKeySpec privateSpec = keyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
		    
		    BigInteger modulus = publicSpec.getModulus();
		    if (modulus.testBit(modulus.bitLength())) {
		    	modulus = modulus.not();
		    }
		    fos.write(MagicSigUtil.encode(modulus.toByteArray()));
		    
		    fos.write(".".getBytes("ASCII"));
		    
		    fos.write(MagicSigUtil.encode(publicSpec.getPublicExponent().toByteArray()));
	
		    fos.write(".".getBytes("ASCII"));
		    
		    fos.write(MagicSigUtil.encode(privateSpec.getPrivateExponent().toByteArray()));
		    
		    fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
