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

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.Signature;

@XmlRootElement(name="env", namespace=XMLMagicEnvelope.NS_MAGIC_ENVELOPE)
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLMagicEnvelope {
	
	public static final String NS_MAGIC_ENVELOPE = "http://salmon-protocol.org/ns/magic-env";
	
	@XmlElement(name="data", namespace=NS_MAGIC_ENVELOPE)
	private XMLMagicEnvelopeData data;
	
	@XmlElement(name="encoding", namespace=NS_MAGIC_ENVELOPE)
	private String encoding;
	
	@XmlElement(name="alg", namespace=NS_MAGIC_ENVELOPE)
	private String alg;
	
	@XmlElement(name="sig", namespace=NS_MAGIC_ENVELOPE)
	private List<XMLMagicEnvelopeSignature> sigs;

	/*
	public XMLMagicEnvelope withSignature(XMLMagicEnvelopeSignature value) {
		if (null == this.sigs) {
			this.sigs = new LinkedList<XMLMagicEnvelopeSignature>();
		}
		this.sigs.add(value);
		return this;
	}
	
	public byte[] toBytes() throws MagicSignatureException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(XMLMagicEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.marshal(this, baos);
			return baos.toByteArray();
		} catch (JAXBException e) {
			throw new MagicSignatureException("Unable to serialize Magic Envelope", e);
		}
	}
	*/
	public String getAlgorithm() {
		return this.alg;
	}

	public List<XMLMagicEnvelopeSignature> getSignatures() {
		return this.sigs;
	}

	public String getType() {
		if (null == this.data) { return null; }
		return this.data.getType();
	}
	
	public String getData() {
		if (null == this.data) { return null; }
		return this.data.getValue();
	}

	public String getEncoding() {
		return this.encoding;
	}
}
