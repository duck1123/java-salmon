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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cliqset.magicsig.guice.DefaultMagicSigModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class MagicSig {

	private static Logger logger = LoggerFactory.getLogger(MagicSig.class);
	
	private Map<String, MagicSigAlgorithm> algorithms;
	private Map<String, MagicSigEncoding> encodings;
	
	private PayloadToMetadataMapper payloadToMetadataMapper;
	
	@Inject
	protected MagicSig(Map<String, MagicSigAlgorithm> algorithms, 
					Map<String, MagicSigEncoding> encodings,
					PayloadToMetadataMapper payloadToMetadataMapper) {
		this.algorithms = algorithms;
		this.encodings = encodings;
		this.payloadToMetadataMapper = payloadToMetadataMapper;
	}

	public byte[] decodeData(MagicEnvelope envelope) throws MagicSigException {
		MagicSigEncoding encoder = this.encodings.get(envelope.getEncoding());
		if (null == encoder) {
			throw new MagicSigException("Unrecognized encoding:" + envelope.getEncoding());
		}
		
		return encoder.decode(envelope.getData());
	}
	
	public MagicEnvelope sign(byte[] data, Key key, String algorithm, String encoding, String dataType) throws MagicSigException {
		MagicSigEncoding encoder = this.encodings.get(encoding);
		if (null == encoder) {
			throw new MagicSigException("Unrecognized encoding:" + encoding);
		}
		
		MagicSigAlgorithm alg = this.algorithms.get(algorithm);
		if (null == alg) {
			throw new MagicSigException("Unrecognized algorithm:" + algorithm);
		}

		byte[] signatureData = buildSigBaseString(data, dataType, encoding, algorithm);

		MagicEnvelope env = new MagicEnvelope()
			.withAlgorithm(algorithm)
			.withData(encoder.encodeToString(data))
			.withDataType(dataType)
			.withEncoding(encoding)
			.withSignature(new Signature()
				.withKeyId(key.getKeyId())
				.withValue(encoder.encodeToString(alg.sign(signatureData, key))));
		
		return env;
	}
	
	public EnvelopeVerificationResult verify(MagicEnvelope envelope) throws MagicSigException {
		List<Key> keys = this.payloadToMetadataMapper.getKeys(envelope.getDataType(), this.encodings.get(envelope.getEncoding()).decode(envelope.getData()));
		return verify(envelope, keys);
	}
	
	public EnvelopeVerificationResult verify(MagicEnvelope envelope, List<? extends Key> keys) throws MagicSigException {
		MagicSigEncoding encoder = this.encodings.get(envelope.getEncoding());
		if (null == encoder) {
			throw new MagicSigException("Unrecognized encoding:" + envelope.getEncoding());
		}
		
		MagicSigAlgorithm algorithm = this.algorithms.get(envelope.getAlgorithm());
		if (null == algorithm) {
			throw new MagicSigException("Unrecognized algorithm:" + envelope.getAlgorithm());
		}
		
		byte[] data = encoder.decode(envelope.getData());
		
		byte[] dataForSig;
		
		try {
			dataForSig = buildSigBaseString(data, envelope.getDataType(), envelope.getEncoding(), envelope.getAlgorithm());
		} catch (Exception e) {
			throw new MagicSigException("Unable to build data for signature verification.", e);
		}
		
		EnvelopeVerificationResult result = new EnvelopeVerificationResult()
			.withData(data);
			
		for (Signature sig : envelope.getSignatures()) {
			logger.debug("verifying signature:{} with keyId:", sig.getValue(), sig.getKeyId());
		
			logger.debug("Verifying signature with " + keys.size() + " keys");
			
			Key verifiedKey = null;
			
			for (Key key : keys) {
				if (!key.supportsKeyId() || sig.getKeyId() == null || sig.getKeyId().equals("") || sig.getKeyId().equals(key.getKeyId())) {
					logger.debug("Attempting to verify signature with:{}", key.toString());
					if (algorithm.verify(dataForSig, encoder.decode(sig.getValue()), key)) {
						verifiedKey = key;
						break;
					} else {
						logger.info("Key with keyId {} matched Signature keyId {}, but didn't verify.", key.toString(), sig.getKeyId());
					}
				} else {
					logger.debug("Did not attempt to verify key {} because the keyid does not match signature keyId of {}", key.toString(), sig.getKeyId());
				}
			}
			result.withSignatureVerificationResult(new SignatureVerificationResult()
				.withSignature(sig)
				.withKey(verifiedKey));
		}
		return result;
	}	
	
	private byte[] buildSigBaseString(byte[] data, String dataType, String encoding, String algorithm) throws MagicSigException {
		MagicSigEncoding encoder = this.encodings.get(encoding);
		if (null == encoder) {
			throw new MagicSigException("Unrecognized encoding:" + encoding);
		}
		
		try {
			StringBuilder sb = new StringBuilder(encoder.encodeToString(data));
			sb.append(".");
			sb.append(encoder.encodeToString(dataType.getBytes("ASCII")));
			sb.append(".");
			sb.append(encoder.encodeToString(encoding.getBytes("ASCII")));
			sb.append(".");
			sb.append(encoder.encodeToString(algorithm.getBytes("ASCII")));
			
			return sb.toString().getBytes("ASCII");
		} catch (UnsupportedEncodingException uee) {
			throw new MagicSigException("ASCII is not supported on this system.", uee);
		}
	}
	
	public static MagicSig getDefault() {
		Injector i = Guice.createInjector(new DefaultMagicSigModule());
		return i.getInstance(MagicSig.class);
	}
}
