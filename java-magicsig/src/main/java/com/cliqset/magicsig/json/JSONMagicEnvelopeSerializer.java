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
package com.cliqset.magicsig.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigConstants;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;

import com.google.gson.Gson;

public class JSONMagicEnvelopeSerializer implements MagicEnvelopeSerializer {

	public static final String MEDIA_TYPE = MagicSigConstants.MEDIA_TYPE_MAGIC_ENV_JSON;

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
