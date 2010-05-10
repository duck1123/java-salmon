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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

@XmlRootElement(name="env", namespace=MagicEnvelope.NS_MAGIC_ENVELOPE)
@XmlAccessorType(XmlAccessType.FIELD)
public class MagicEnvelope {
	
	public static final String NS_MAGIC_ENVELOPE = "http://salmon-protocol.org/ns/magic-env";
	
	@XmlElement(name="data", namespace=NS_MAGIC_ENVELOPE)
	private MagicEnvelopeData data;
	
	@XmlElement(name="encoding", namespace=NS_MAGIC_ENVELOPE)
	private String encoding;
	
	@XmlElement(name="alg", namespace=NS_MAGIC_ENVELOPE)
	private String alg;
	
	@XmlElement(name="sig", namespace=NS_MAGIC_ENVELOPE)
	private MagicEnvelopeSignature sig;

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
	
	public void setData(MagicEnvelopeData data) {
		this.data = data;
	}

	public MagicEnvelopeData getData() {
		return data;
	}
	
	public void setSig(MagicEnvelopeSignature sig) {
		this.sig = sig;
	}

	public MagicEnvelopeSignature getSig() {
		return sig;
	}
	
	public byte[] toBytes() throws SalmonException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(MagicEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MagicEnvelopePrefixMapper());
			m.marshal(this, baos);
			return baos.toByteArray();
		} catch (JAXBException e) {
			throw new SalmonException("Unable to serialize Magic Envelope", e);
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
			if (NS_MAGIC_ENVELOPE.equals(namespaceUri)) {
				return "me";
			}
			return null;
		}
	}
}
