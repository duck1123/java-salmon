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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializationProvider;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSig;

public class Signer {

	private static String filename = "/DemoEntry.txt";
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				filename = args[0];
			}
			
			byte[] bytes = getBytes(filename);
			
			MagicKey key = new MagicKey(getBytes("/DemoKeys.txt"));
			
			MagicSig magicSig = MagicSig.getDefault();
			
			MagicEnvelope env = magicSig.sign(bytes, key, "RSA-SHA256", "base64url", "application/atom+xml");
			
			FileOutputStream fos = new FileOutputStream(filename + ".env");
			
			MagicEnvelopeSerializer serializer = MagicEnvelopeSerializationProvider.getDefault().getSerializer(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML);
			serializer.serialize(env, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private static byte[] getBytes(String filename) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = new FileInputStream(filename);
		try {
			int i;
			while (-1 != (i = data.read(buffer, 0, 512))) {
				baos.write(buffer, 0, i);
			}
		} catch (IOException e) {
			
		}
		return baos.toByteArray();
	}
}
