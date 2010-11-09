package com.cliqset.magicsig.xml;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
