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

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;

public class XMLMagicEnvelopeDeserializer implements MagicEnvelopeDeserializer {

	public static final String MEDIA_TYPE = MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_XML;
	
	public MagicEnvelope deserialize(InputStream is) throws MagicSigException {
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMagicEnvelope.class);
			XMLMagicEnvelope xmlEnv = (XMLMagicEnvelope)context.createUnmarshaller().unmarshal(is);
			MagicEnvelope me = new MagicEnvelope()
						.withAlgorithm(xmlEnv.getAlgorithm())
						.withEncoding(xmlEnv.getEncoding())
						.withData(xmlEnv.getData())
						.withDataType(xmlEnv.getType());
			for (XMLMagicEnvelopeSignature s : xmlEnv.getSignatures()) {
				me.withSignature(new Signature().withKeyId(s.getKeyId()).withValue(s.getValue()));
			}

			return me;
		} catch (JAXBException je) {
			throw new MagicSigException("Unable to parse as a Magic Envelope", je);
		}
	}
}