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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicSigner {

	private static Logger logger = LoggerFactory.getLogger(MagicSigner.class);
	
	private Map<String, MagicSignatureAlgorithm> algorithms = new HashMap<String, MagicSignatureAlgorithm>();
	private Map<String, MagicSignatureEncoding> encoders = new HashMap<String, MagicSignatureEncoding>();
	
	public MagicSigner withAlgorithm(MagicSignatureAlgorithm algorithm) {
		algorithms.put(algorithm.getIdentifier(), algorithm);
		return this;
	}
	
	public MagicSigner withEncoding(MagicSignatureEncoding encoding) {
		encoders.put(encoding.getIdentifier(), encoding);
		return this;
	}
	
	public byte[] decodeData(MagicEnvelope envelope) throws MagicSignatureException {
		MagicSignatureEncoding encoder = this.encoders.get(envelope.getEncoding());
		if (null == encoder) {
			throw new MagicSignatureException("Unrecognized encoding:" + envelope.getEncoding());
		}
		
		return encoder.decode(envelope.getData());
	}
	
	public MagicEnvelope sign(byte[] data, Key key, String algorithm, String encoding, String dataType) throws Exception {
		MagicSignatureEncoding encoder = this.encoders.get(encoding);
		if (null == encoder) {
			throw new MagicSignatureException("Unrecognized encoding:" + encoding);
		}
		
		MagicSignatureAlgorithm alg = this.algorithms.get(algorithm);
		if (null == alg) {
			throw new MagicSignatureException("Unrecognized algorithm:" + algorithm);
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
	
	public EnvelopeVerificationResult verify(MagicEnvelope envelope, List<? extends Key> keys) throws MagicSignatureException {
		MagicSignatureEncoding encoder = this.encoders.get(envelope.getEncoding());
		if (null == encoder) {
			throw new MagicSignatureException("Unrecognized encoding:" + envelope.getEncoding());
		}
		
		MagicSignatureAlgorithm algorithm = this.algorithms.get(envelope.getAlgorithm());
		if (null == algorithm) {
			throw new MagicSignatureException("Unrecognized algorithm:" + envelope.getAlgorithm());
		}
		
		byte[] data = encoder.decode(envelope.getData());
		
		byte[] dataForSig;
		
		try {
			dataForSig = buildSigBaseString(data, envelope.getDataType(), envelope.getEncoding(), envelope.getAlgorithm());
		} catch (Exception e) {
			throw new MagicSignatureException("Unable to build data for signature verification.", e);
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
	
	private byte[] buildSigBaseString(byte[] data, String dataType, String encoding, String algorithm) throws MagicSignatureException {
		MagicSignatureEncoding encoder = this.encoders.get(encoding);
		if (null == encoder) {
			throw new MagicSignatureException("Unrecognized encoding:" + encoding);
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
			throw new MagicSignatureException("ASCII is not supported on this system.", uee);
		}
	}
}
