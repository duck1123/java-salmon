package com.cliqset.magicsig.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.cliqset.magicsig.MagicEnvelopeDeserializer;
import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSignatureException;
import com.cliqset.magicsig.Signature;
import com.google.gson.Gson;

public class JSONMagicEnvelopeDeserializer implements MagicEnvelopeDeserializer {

	public MagicEnvelope deserialize(InputStream is) throws MagicSignatureException {
		JSONMagicEnvelope jsonEnv = new Gson().fromJson(new InputStreamReader(is), JSONMagicEnvelope.class);
		
		//TODO: validate required elements are present
		
		MagicEnvelope me = new MagicEnvelope()
								.withData(jsonEnv.getData())
								.withDataType(jsonEnv.getData_type())
								.withEncoding(jsonEnv.getEncoding())
								.withAlgorithm(jsonEnv.getAlg());
		for (JSONMagicEnvelopeSignature s : jsonEnv.getSigs()) {
			me.withSignature(new Signature().withKeyId(s.getKey_id()).withValue(s.getValue()));
		}
		return me;
	}

	public List<String> getSupportedMediaTypes() {
		return Collections.unmodifiableList(Arrays.asList(new String[] {MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_JSON}));
	}
}
