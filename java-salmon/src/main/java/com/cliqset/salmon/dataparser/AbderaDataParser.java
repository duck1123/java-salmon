package com.cliqset.salmon.dataparser;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.iri.IRISyntaxException;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Person;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

import com.cliqset.salmon.DataParser;
import com.cliqset.salmon.SalmonException;

public class AbderaDataParser implements DataParser {

	public static final String MATCHING_MIME_TYPE = "application/atom+xml";
	private static final Abdera abdera = Abdera.getInstance();
	
	
	public URI getSignerUri(byte[] data) throws SalmonException {
		Entry entry = null;
		try {
			Parser parser = abdera.getParser();
			Document<Entry> entryDoc = parser.parse(new ByteArrayInputStream(data));
			entry = entryDoc.getRoot();
		} catch (ParseException pe) {
			throw new SalmonException("Unable to parse salmon as ATOM entry", pe);
		}
		
		Person author = entry.getAuthor();
		if (null == author) {
			throw new SalmonException("No author element on the entry.");
		}
		
		IRI authorIRI = null;
		try {
			authorIRI = author.getUri();
			return new URI(authorIRI.toString());
		} catch (IRISyntaxException ise) {
			throw new SalmonException("Invalid author URI", ise);
		} catch (URISyntaxException use) {
			throw new SalmonException("Invalid author URI", use);
		}
	}

	public boolean parsesMimeType(String mimeType) {
		return MATCHING_MIME_TYPE.equals(mimeType);
 	}
}