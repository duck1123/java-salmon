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

package com.cliqset.salmon.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.MagicSigAlgorithm;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSig;
import com.cliqset.magicsig.MagicSigEncoding;
import com.cliqset.magicsig.URIPayloadToMetadataMapper;
import com.cliqset.magicsig.algorithm.RSASHA256MagicSigAlgorithm;
import com.cliqset.magicsig.encoding.Base64URLMagicSigEncoding;
import com.cliqset.magicsig.xml.XMLMagicEnvelopeDeserializer;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.dataparser.AbderaDataParser;
import com.cliqset.salmon.keyfinder.OpenXRDKeyFinder;

public class Verifier {
	
	private static String filename = "/DemoEntry.txt.env";
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				filename = args[0];
			}
			
			MagicEnvelope.withDeserializer(new XMLMagicEnvelopeDeserializer());
			MagicEnvelope envelope = MagicEnvelope.fromInputStream(MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML, new FileInputStream(filename));
			MagicKey key = new MagicKey(getBytes("/DemoKeys.txt"));
			List<Key> keys = new ArrayList<Key>();
			keys.add(key);
			
			Map<String, MagicSigAlgorithm> algorithms = new HashMap<String, MagicSigAlgorithm>();
			algorithms.put("RSA-SHA256", new RSASHA256MagicSigAlgorithm());
			
			Map<String, MagicSigEncoding> encodings = new HashMap<String, MagicSigEncoding>();
			encodings.put("base64url", new Base64URLMagicSigEncoding());
			
			Set<DataParser> dataParsers = new HashSet<DataParser>();
			
			Set<KeyFinder> keyFinders = new HashSet<KeyFinder>();
			
			MagicSig magicSig = new MagicSig(algorithms, encodings, new URIPayloadToMetadataMapper(dataParsers, keyFinders));
			
			Salmon s = new Salmon(magicSig);
			System.out.println(s.verify(envelope));
			
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
