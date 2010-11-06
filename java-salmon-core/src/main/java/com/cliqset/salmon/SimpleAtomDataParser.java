package com.cliqset.salmon;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.MagicSignatureException;

public class SimpleAtomDataParser implements DataParser {

	public URI getSignerUri(byte[] data) throws MagicSignatureException {
		try {
			JAXBContext context = JAXBContext.newInstance(AtomEntry.class);
			AtomEntry e = (AtomEntry)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(data));
			if (null == e.Author) {
				throw new MagicSignatureException("Cannot extract signer URI, no author element on this entry.");
			}
			if (null == e.Author.URI) {
				throw new MagicSignatureException("Cannot extract signer URI, author element has no uri element.");
			}
	
			return new URI(e.Author.URI);
		} catch (JAXBException je) {
			throw new MagicSignatureException("Cannot extract signer URI, unable to deserialize atom entry.", je);
		} catch (URISyntaxException use) {
			throw new MagicSignatureException("Cannot extract signer URI, author uri element is not a valid URI.", use);
		}	
	}

	public boolean parsesMimeType(String mimeType) {
		return "application/atom+xml".equals(mimeType);
	}
}
