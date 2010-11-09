package com.cliqset.magicsig;

import java.io.OutputStream;

public interface MagicEnvelopeSerializer {
	
	void serialize(MagicEnvelope env, OutputStream os) throws MagicSigException;
}
