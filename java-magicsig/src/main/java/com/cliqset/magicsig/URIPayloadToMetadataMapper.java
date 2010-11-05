package com.cliqset.magicsig;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;

public class URIPayloadToMetadataMapper implements PayloadToMetadataMapper {

	private List<DataParser> dataParsers = new LinkedList<DataParser>();
	
	private List<KeyFinder> keyFinders = new LinkedList<KeyFinder>();
	
	public URIPayloadToMetadataMapper withDataParser(DataParser parser) {
		if (null == parser) {
			throw new IllegalArgumentException("parser must not be null.");
		}
		dataParsers.add(parser);
		return this;
	}
	
	public URIPayloadToMetadataMapper withKeyFinder(KeyFinder keyFinder) {
		if (null == keyFinder) {
			throw new IllegalArgumentException("keyfinder must not be null.");
		}
		keyFinders.add(keyFinder);
		return this;
	}
	
	public List<Key> getKeys(String mediaType, byte[] data) throws MagicSignatureException {
		URI signerURI = extractSignerUri(mediaType, data);
        
        List<Key> signerKeys = findKeys(signerURI);
        
        return signerKeys;
	}

	private URI extractSignerUri(String mimeType, byte[] data) throws MagicSignatureException {
		for (DataParser parser : this.dataParsers) {
			if (parser.parsesMimeType(mimeType)) {
				try {
					return parser.getSignerUri(data);
				} catch (Exception e) { 
					//ignore and try the next one
				}
			}
		}
		throw new MagicSignatureException("Unable to extract signer URI from data.");
	}
	
	private List<Key> findKeys(URI authorUri) throws MagicSignatureException {
		for (KeyFinder finder : this.keyFinders) {
			try {
				List<Key> keys = finder.findKeys(authorUri); 
				if (null != keys && keys.size() > 0) { return keys; }
			} catch (Exception e) { 
				//ignore and try the next one
			}
		}
		throw new MagicSignatureException("Unable to find keys for signer: " + authorUri.toString());
	}
}
