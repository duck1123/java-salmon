package com.cliqset.magicsig;

import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class PKIKey extends Key {

	public abstract PrivateKey getPrivateKey();
	
	public abstract PublicKey getPublicKey();
	
	public abstract String getType();
	
	public abstract boolean hasPrivateKey();
}
