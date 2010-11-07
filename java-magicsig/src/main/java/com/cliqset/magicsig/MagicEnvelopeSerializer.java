package com.cliqset.magicsig;

import java.io.OutputStream;
import java.util.List;

public interface MagicEnvelopeSerializer {
	
	List<String> getSupportedMediaTypes();
	
	void serialize(MagicEnvelope env, OutputStream os) throws MagicSigException;
}
