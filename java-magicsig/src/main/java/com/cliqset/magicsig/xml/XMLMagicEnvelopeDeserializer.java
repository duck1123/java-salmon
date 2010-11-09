package com.cliqset.magicsig.xml;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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