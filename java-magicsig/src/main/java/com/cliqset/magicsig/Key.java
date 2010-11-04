package com.cliqset.magicsig;

public abstract class Key {
	
	public boolean supportsKeyId() {
		return false;
	}
	
	public String getKeyId() {
		return null;
	}
}
