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

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.MagicSigException;

public class AbderaDataParser implements DataParser {

	public static final String MATCHING_MIME_TYPE = "application/atom+xml";
	private static final Abdera abdera = Abdera.getInstance();
	
	
	public URI getSignerUri(byte[] data) throws MagicSigException {
		Entry entry = null;
		try {
			Parser parser = abdera.getParser();
			Document<Entry> entryDoc = parser.parse(new ByteArrayInputStream(data));
			entry = entryDoc.getRoot();
		} catch (ParseException pe) {
			throw new MagicSigException("Unable to parse salmon as ATOM entry", pe);
		}
		
		Person author = entry.getAuthor();
		if (null == author) {
			throw new MagicSigException("No author element on the entry.");
		}
		
		IRI authorIRI = null;
		try {
			authorIRI = author.getUri();
			return new URI(authorIRI.toString());
		} catch (IRISyntaxException ise) {
			throw new MagicSigException("Invalid author URI", ise);
		} catch (URISyntaxException use) {
			throw new MagicSigException("Invalid author URI", use);
		}
	}

	public boolean parsesMimeType(String mimeType) {
		return MATCHING_MIME_TYPE.equals(mimeType);
 	}
}
