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
