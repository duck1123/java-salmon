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

package com.cliqset.magicsig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	private List<MagicEnvelopeSignature> sigs;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public MagicEnvelope withData(MagicEnvelopeData value) {
		this.data = value;
		return this;
	}
	
	public MagicEnvelope withEncoding(String value) {
		this.encoding = value;
		return this;
	}
	
	public MagicEnvelope withAlgorithm(String value) {
		this.alg = value;
		return this;
	}
	
	public MagicEnvelope withSignature(MagicEnvelopeSignature value) {
		if (null == this.sigs) {
			this.sigs = new LinkedList<MagicEnvelopeSignature>();
		}
		this.sigs.add(value);
		return this;
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

	public List<MagicEnvelopeSignature> getSigs() {
		return sigs;
	}
	
	public byte[] toBytes() throws MagicSignatureException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(MagicEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.marshal(this, baos);
			return baos.toByteArray();
		} catch (JAXBException e) {
			throw new MagicSignatureException("Unable to serialize Magic Envelope", e);
		}
	}
	
	public static MagicEnvelope fromInputStream(InputStream is) throws MagicSignatureException {
		try {
			JAXBContext context = JAXBContext.newInstance(MagicEnvelope.class);
			return (MagicEnvelope)context.createUnmarshaller().unmarshal(is);
		} catch (JAXBException je) {
			throw new MagicSignatureException("Unable to parse as a Magic Envelope", je);
		}
	}
	
	public static MagicEnvelope fromBytes(byte[] bytes) throws MagicSignatureException {
		return fromInputStream(new ByteArrayInputStream(bytes));
	}
}
