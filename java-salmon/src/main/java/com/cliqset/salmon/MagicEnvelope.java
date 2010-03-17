package com.cliqset.salmon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

@XmlRootElement(name="env", namespace="http://salmon-protocol.org/ns/magic-env")
@XmlAccessorType(XmlAccessType.FIELD)
public class MagicEnvelope {
	
	@XmlElement(name="data", namespace="http://salmon-protocol.org/ns/magic-env")
	private MagicEnvelopeData data;
	
	@XmlElement(name="encoding", namespace="http://salmon-protocol.org/ns/magic-env")
	private String encoding;
	
	@XmlElement(name="alg", namespace="http://salmon-protocol.org/ns/magic-env")
	private String alg;
	
	@XmlElement(name="sig", namespace="http://salmon-protocol.org/ns/magic-env")
	private String sig;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getAlg() {
		return alg;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public String getSig() {
		return sig;
	}
	
	public void setData(MagicEnvelopeData data) {
		this.data = data;
	}

	public MagicEnvelopeData getData() {
		return data;
	}
	
	public byte[] toBytes() throws SalmonException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(MagicEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MagicEnvelopePrefixMapper());
			m.marshal(this, baos);
			return baos.toByteArray();
		} catch (JAXBException e) {
			throw new SalmonException("Unable to descerialize Magic Envelope", e);
		}
	}
	
	public static MagicEnvelope fromInputStream(InputStream is) throws SalmonException {
		try {
			JAXBContext context = JAXBContext.newInstance(MagicEnvelope.class);
			return (MagicEnvelope)context.createUnmarshaller().unmarshal(is);
		} catch (JAXBException je) {
			throw new SalmonException("Unable to parse as a Magic Envelope", je);
		}
	}
	
	public static MagicEnvelope fromBytes(byte[] bytes) throws SalmonException {
		return fromInputStream(new ByteArrayInputStream(bytes));
	}

	public static class MagicEnvelopePrefixMapper extends NamespacePrefixMapper {

		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
			if ("http://salmon-protocol.org/ns/magic-env".equals(namespaceUri)) {
				return "me";
			}
			return suggestion;
		}
	}
}
