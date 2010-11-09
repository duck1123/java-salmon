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

package com.cliqset.magicsig.encoding;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.cliqset.magicsig.MagicSigEncoding;

public class Base64URLMagicSigEncoding implements MagicSigEncoding {

	public static final String ENCODING_IDENTIFIER = "base64url";
	
	public byte[] decode(String armoredData) {
		//remove whitespace
		armoredData = armoredData.replace("\\s", "");
		
		//pad so length is divisible by 4
		while (armoredData.length() % 4 != 0) {
			armoredData += (char)61;
		}

		return Base64.decodeBase64(armoredData);
	}

	public byte[] decode(byte[] armoredDataBytes) {
		String armoredData = null;
		try {
			armoredData = new String(armoredDataBytes, "ASCII");
		} catch (Exception e) {}
		return decode(armoredData);
	}

	public byte[] encode(byte[] data) {
		try {
			return encodeToString(data).getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public String encodeToString(byte[] data) {
		String b64 = Base64.encodeBase64URLSafeString(data);
		//looks like encodeBase64URLSafeString doesn't pad to / 4
		while (b64.length() % 4 != 0) {
			b64 += (char)61;
		}
		return b64;
	}
}
