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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;

import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigner;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSignatureAlgorithm;

public class KeyTester {
	
	private static final String keyFile = "/magickeys.txt";
	private static final byte[] data = "This is the data!".getBytes();

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(keyFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			
			MagicSigner magicSig = new MagicSigner();
			
			while ((line = reader.readLine()) != null ) {
				MagicKey key = new MagicKey(line.getBytes("ASCII"));
				
				RSASHA256MagicSignatureAlgorithm alg = new RSASHA256MagicSignatureAlgorithm();
				byte[] sig = alg.sign(data, key);
				
				System.out.println(Base64.encodeBase64URLSafeString(sig));
				
				boolean verified = alg.verify(data, sig, key);
				
				if (!verified) {
					System.out.println("FAILED - " + line);
				}
				
				//System.out.println(lineSplit[0] + " " + key.toString(true));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
	}
}
