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

package com.cliqset.magicsig.util;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.apache.commons.codec.binary.Base64;

public class KeyGen {

	private static final int keySize = 1024;
	private static final int numKeys = 1;
	
	private static final String fileName = "/magickeys.txt";
	
	public static void main(String[] args) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			for (int x = 0; x < numKeys; x++) {
				fos.write((x + " RSA.").getBytes("ASCII"));
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			    keyPairGenerator.initialize(keySize);
			    KeyPair keyPair = keyPairGenerator.genKeyPair();
		
			    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			    RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
			    RSAPrivateKeySpec privateSpec = keyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
			    
			    fos.write(Base64.encodeBase64URLSafe(getBytes(publicSpec.getModulus())));
			    
			    fos.write(".".getBytes("ASCII"));
			    
			    fos.write(Base64.encodeBase64URLSafe(getBytes(publicSpec.getPublicExponent())));
		
			    fos.write(".".getBytes("ASCII"));
			    
			    fos.write(Base64.encodeBase64URLSafe(getBytes(privateSpec.getPrivateExponent())));
			    
			    fos.write("\n".getBytes("ASCII"));
			    
			    System.out.println(x);
			}
		    fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Copyright  1999-2009 The Apache Software Foundation.
	 *
	 *  Licensed under the Apache License, Version 2.0 (the "License");
	 *  you may not use this file except in compliance with the License.
	 *  You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 *  Unless required by applicable law or agreed to in writing, software
	 *  distributed under the License is distributed on an "AS IS" BASIS,
	 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 *  See the License for the specific language governing permissions and
	 *  limitations under the License.
	 *
	 */
	/**
	 * The function below was adapted from com/sun/org/apache/xml/internal/security/utils/Base64.java
	 */
	/**
	 * Implementation of MIME's Base64 encoding and decoding conversions.
	 * Optimized code. (raw version taken from oreilly.jonathan.util, 
	 * and currently org.apache.xerces.ds.util.Base64)
	 *
	 * @author Raul Benito(Of the xerces copy, and little adaptations).
	 * @author Anli Shundi
	 * @author Christian Geuer-Pollmann
	 * @see <A HREF="ftp://ftp.isi.edu/in-notes/rfc2045.txt">RFC 2045</A>
	 * @see org.apache.xml.security.transforms.implementations.TransformBase64Decode
	 */
	/**
	* Returns a byte-array representation of a <code>{@link BigInteger}<code>.
	* No sign-bit is outputed.
	*
	* <b>N.B.:</B> <code>{@link BigInteger}<code>'s toByteArray
	* retunrs eventually longer arrays because of the leading sign-bit.
	*
	* @param big <code>BigInteger<code> to be converted
	* @param bitlen <code>int<code> the desired length in bits of the representation
	* @return a byte array with <code>bitlen</code> bits of <code>big</code>
	*/
	static final byte[] getBytes(BigInteger big) {
		  int bitlen = big.bitLength();
		  //round bitlen
		  bitlen = ((bitlen + 7) >> 3) << 3;
		
		  if (bitlen < big.bitLength()) {
		     throw new IllegalArgumentException("Illegal bit len.");
		  }
		
		  byte[] bigBytes = big.toByteArray();
		
		  if (((big.bitLength() % 8) != 0)
		          && (((big.bitLength() / 8) + 1) == (bitlen / 8))) {
		     return bigBytes;
		  }
		
		  // some copying needed
		  int startSrc = 0;    // no need to skip anything
		  int bigLen = bigBytes.length;    //valid length of the string
		
		  if ((big.bitLength() % 8) == 0) {    // correct values
			 startSrc = 1;    // skip sign bit
			
			 bigLen--;    // valid length of the string
		  }
		
		  int startDst = bitlen / 8 - bigLen;    //pad with leading nulls
	      byte[] resizedBytes = new byte[bitlen / 8];
	
	      System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, bigLen);
	
	      return resizedBytes;
	}
}
