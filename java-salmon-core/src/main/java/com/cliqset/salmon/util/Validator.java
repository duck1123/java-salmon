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

package com.cliqset.salmon.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigner;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.SalmonException;

public class Validator {

	private static String filename = "/DemoEnvelope.txt";
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				filename = args[0];
			}

			Salmon salmon = new Salmon(new MagicSigner().withPayloadToMetadataMapper(new URIPayloadToMetadataMapper()
				.withKeyFinder(new KeyFinder() {

					public List<Key> findKeys(URI signerUri) throws MagicSignatureException {
						List<Key> keys = new LinkedList<Key>();
						try {
							keys.add(new MagicKey(getBytes("/DemoKeys.txt")));
						} catch (Exception e) {
							throw new MagicSignatureException("Couldn't read the keys!");
						}
						return keys;
						
					}
				
				})
				.withDataParser(new DataParser() {

					public URI getSignerUri(byte[] data) throws MagicSignatureException {
						try {
							return new URI("doesnt@matter.com");
						} catch (Exception e) {
							throw new MagicSignatureException("Couldn't create the URI!");
						}
					}

					public boolean parsesMimeType(String mimeType) {
						return true;
					}					
				})));
			MagicEnvelope.withDeserializer(new XMLMagicEnvelopeDeserializer());
			byte[] output = salmon.verify(MagicEnvelope.fromInputStream(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML, new FileInputStream(filename)));
			
			System.out.println(new String(output, "UTF-8"));
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