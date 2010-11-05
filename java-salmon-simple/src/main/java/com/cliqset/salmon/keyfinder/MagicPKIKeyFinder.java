/*
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

package com.cliqset.salmon.keyfinder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.cliqset.hostmeta.HostMeta;
import com.cliqset.hostmeta.HostMetaException;
import com.cliqset.hostmeta.HostMetaHandler;
import com.cliqset.hostmeta.JavaNetXRDFetcher;
import com.cliqset.hostmeta.template.LRDDTemplateProcessor;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.magicsig.Key;
import com.cliqset.magicsig.KeyFinder;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.xrd.Property;
import com.cliqset.magicsig.MagicSigConstants;

public class MagicPKIKeyFinder implements KeyFinder {

	private static HostMeta hostMeta = new HostMeta()
		.withTemplateProcessor("lrdd", new LRDDTemplateProcessor())
		.withXRDFetcher(new JavaNetXRDFetcher());
	
	public List<Key> findKeys(URI uri) throws MagicSignatureException {
		try {
			MagicKeyHandler handler = new MagicKeyHandler();
			hostMeta.discoverResourceSpecific(uri, handler);
			return handler.Keys;
		} catch (HostMetaException hme) {
			throw new MagicSignatureException(hme);
		}
	}
	
	private static class MagicKeyHandler extends HostMetaHandler {

		final List<Key> Keys = new LinkedList<Key>();
		
		public boolean onProperty(Property property) {
			if (MagicSigConstants.MAGIC_KEY_TYPE.equals(property.getType().toString())) {
				try {
					byte[] keyBytes = property.getValue().toString().getBytes("ASCII");
					String keyId = null;
					if (property.hasUnknownAttributes()) {
						keyId = (String)property.getUnknownAttributes().get(MagicSigConstants.KEY_ID_QNAME);
					}
					Keys.add(new MagicKey(keyBytes).withKeyId(keyId));
				} catch (UnsupportedEncodingException uee) { }
			}
			return false;
		}
	}
}
