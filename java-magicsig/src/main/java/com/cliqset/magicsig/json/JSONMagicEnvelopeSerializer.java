package com.cliqset.magicsig.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;

import com.google.gson.Gson;

public class JSONMagicEnvelopeSerializer implements MagicEnvelopeSerializer {

	public List<String> getSupportedMediaTypes() {
		return Collections.unmodifiableList(Arrays.asList(new String[] {MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_JSON}));
	}

	public void serialize(MagicEnvelope env, OutputStream os) throws MagicSigException {
		JSONMagicEnvelope jme = new JSONMagicEnvelope();
		jme.setAlg(env.getAlgorithm());
		jme.setEncoding(env.getEncoding());
		jme.setData_type(env.getDataType());
		jme.setData(env.getData());
		for (Signature s : env.getSignatures()) {
			if (null == jme.getSigs()) {
				jme.setSigs(new LinkedList<JSONMagicEnvelopeSignature>());
			}
			JSONMagicEnvelopeSignature sig = new JSONMagicEnvelopeSignature();
			sig.setKey_id(s.getKeyId());
			sig.setValue(s.getValue());
			jme.getSigs().add(sig);
		}

		String json = new Gson().toJson(jme, JSONMagicEnvelope.class);
		try {
			os.write(json.getBytes("UTF-8"));
		} catch (IOException ioe) {
			throw new MagicSigException("Unable to serialize magic envelope to output stream.", ioe);
		}
	}
}
