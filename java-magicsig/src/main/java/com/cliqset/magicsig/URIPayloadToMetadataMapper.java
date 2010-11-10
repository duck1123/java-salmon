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
package com.cliqset.magicsig;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;
import com.google.inject.Inject;

public class URIPayloadToMetadataMapper implements PayloadToMetadataMapper {

	private Map<String, DataParser> dataParsers;
	private Set<KeyFinder> keyFinders;
	
	@Inject
	protected URIPayloadToMetadataMapper(Map<String, DataParser> dataParsers, Set<KeyFinder> keyFinders) {
		this.dataParsers = dataParsers;
		this.keyFinders = keyFinders;
	}

	public List<Key> getKeys(String mediaType, byte[] data) throws MagicSigException {
		URI signerURI = extractSignerUri(mediaType, data);
        
        List<Key> signerKeys = findKeys(signerURI);
        
        return signerKeys;
	}

	private URI extractSignerUri(String mediaType, byte[] data) throws MagicSigException {
		DataParser parser = this.dataParsers.get(mediaType);
		if (null == parser) {
			throw new MagicSigException("No parser for mediaType: " + mediaType);
		}
		return parser.getSignerUri(data);
	}
	
	private List<Key> findKeys(URI authorUri) throws MagicSigException {
		for (KeyFinder finder : this.keyFinders) {
			try {
				List<Key> keys = finder.findKeys(authorUri); 
				if (null != keys && keys.size() > 0) { return keys; }
			} catch (Exception e) { 
				//ignore and try the next one
			}
		}
		throw new MagicSigException("Unable to find keys for signer: " + authorUri.toString());
	}
}
