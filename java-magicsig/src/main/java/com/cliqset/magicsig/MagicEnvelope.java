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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class MagicEnvelope {

	private static List<MagicEnvelopeDeserializer> deserializers = new LinkedList<MagicEnvelopeDeserializer>();
	
	private static List<MagicEnvelopeSerializer> serializers = new LinkedList<MagicEnvelopeSerializer>();
	
	private String data;
	
	private String dataType;
	
	private String encoding;
	
	private String algorithm;
	
	private List<Signature> signatures;
	
	public MagicEnvelope withData(String value) {
		this.setData(value);
		return this;
	}
	
	public MagicEnvelope withDataType(String value) {
		this.setDataType(value);
		return this;
	}
	
	public MagicEnvelope withEncoding(String value) {
		this.setEncoding(value);
		return this;
	}
	
	public MagicEnvelope withAlgorithm(String value) {
		this.setAlgorithm(value);
		return this;
	}
	
	public MagicEnvelope withSignature(Signature value) {
		this.getSignatures().add(value);
		return this;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAlgorithm() {
		return algorithm;
	}
	
	public List<Signature> getSignatures() {
		if (null == this.signatures) {
			this.signatures = new LinkedList<Signature>();
		}
		return signatures;
	}
	
	public static void withDeserializer(MagicEnvelopeDeserializer deserializer) {
		if (null == deserializer) {
			throw new IllegalArgumentException("deserializer must not be null.");
		}
		deserializers.add(deserializer);
	}
	
	public static void withSerializer(MagicEnvelopeSerializer serializer) {
		if (null == serializer) {
			throw new IllegalArgumentException("serializer must not be null.");
		}
		serializers.add(serializer);
	}
	
	public void writeTo(String mediaType, OutputStream os) throws MagicSigException {
		MagicEnvelopeSerializationProvider.getDefault().getSerializer(mediaType).serialize(this, os);
	}
	
	public static MagicEnvelope fromInputStream(String mediaType, InputStream is) throws MagicSigException {		
		return MagicEnvelopeSerializationProvider.getDefault().getDeserializer(mediaType).deserialize(is);
	}
}
