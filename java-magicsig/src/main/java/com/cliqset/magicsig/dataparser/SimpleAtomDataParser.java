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
package com.cliqset.magicsig.dataparser;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.cliqset.magicsig.DataParser;
import com.cliqset.magicsig.MagicSigException;

public class SimpleAtomDataParser implements DataParser {

	public URI getSignerUri(byte[] data) throws MagicSigException {
		try {
			JAXBContext context = JAXBContext.newInstance(AtomEntry.class);
			AtomEntry e = (AtomEntry)context.createUnmarshaller().unmarshal(new ByteArrayInputStream(data));
			if (null == e.Author) {
				throw new MagicSigException("Cannot extract signer URI, no author element on this entry.");
			}
			if (null == e.Author.URI) {
				throw new MagicSigException("Cannot extract signer URI, author element has no uri element.");
			}
	
			return new URI(e.Author.URI);
		} catch (JAXBException je) {
			throw new MagicSigException("Cannot extract signer URI, unable to deserialize atom entry.", je);
		} catch (URISyntaxException use) {
			throw new MagicSigException("Cannot extract signer URI, author uri element is not a valid URI.", use);
		}	
	}
}
