package com.cliqset.magicsig;

import java.net.URI;
import java.util.List;
import java.util.Set;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.KeyFinder;

public class URIPayloadToMetadataMapper implements PayloadToMetadataMapper {

	private Set<DataParser> dataParsers;
	
	private Set<KeyFinder> keyFinders;
	
	public URIPayloadToMetadataMapper(Set<DataParser> dataParsers, Set<KeyFinder> keyFinders) {
		this.keyFinders = keyFinders;
		this.dataParsers = dataParsers;
	}
	/*
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
	*/
	public List<Key> getKeys(String mediaType, byte[] data) throws MagicSigException {
		URI signerURI = extractSignerUri(mediaType, data);
        
        List<Key> signerKeys = findKeys(signerURI);
        
        return signerKeys;
	}

	private URI extractSignerUri(String mimeType, byte[] data) throws MagicSigException {
		for (DataParser parser : this.dataParsers) {
			if (parser.parsesMimeType(mimeType)) {
				try {
					return parser.getSignerUri(data);
				} catch (Exception e) { 
					//ignore and try the next one
				}
			}
		}
		throw new MagicSigException("Unable to extract signer URI from data.");
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
