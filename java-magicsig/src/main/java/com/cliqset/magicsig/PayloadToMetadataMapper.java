package com.cliqset.magicsig;

import java.util.List;

//TODO: come up with a better name for this : PayloadToMetadataMapper
public interface PayloadToMetadataMapper {
	
	//include alorithm? and other hints
	List<Key> getKeys(String mediaType, byte[] data) throws MagicSignatureException;
}
