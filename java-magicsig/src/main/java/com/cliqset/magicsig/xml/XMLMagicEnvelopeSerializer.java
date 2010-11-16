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
package com.cliqset.magicsig.xml;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;

public class XMLMagicEnvelopeSerializer implements MagicEnvelopeSerializer {

	public static final String MEDIA_TYPE = MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML;

	public void serialize(MagicEnvelope env, OutputStream os) throws MagicSigException {
		if (null == env) { throw new IllegalArgumentException("Cannot serialize a null magic envelope."); }
		if (null == os) { throw new IllegalArgumentException("Cannot serialize to a null output stream."); }
		
		try {
			XMLMagicEnvelope xme = new XMLMagicEnvelope();
			xme.setAlgorithm(env.getAlgorithm());
			xme.setEncoding(env.getEncoding());
			xme.setType(env.getDataType());
			xme.setData(env.getData());
			for (Signature s : env.getSignatures()) {
				xme.getSignatures().add(new XMLMagicEnvelopeSignature().withKeyId(s.getKeyId()).withValue(s.getValue()));
			}
			JAXBContext context = JAXBContext.newInstance(XMLMagicEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.marshal(xme, os);
		} catch (JAXBException e) {
			throw new MagicSigException("Unable to serialize Magic Envelope", e);
		}
	}
}
