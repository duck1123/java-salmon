package com.cliqset.salmon;

public class SalmonException extends Exception {

	private static final long serialVersionUID = -4212775834282018370L;

	public SalmonException() {
		super();
	}
	
	public SalmonException(String message) {
		super(message);
	}
	
	public SalmonException(Throwable inner) {
		super(inner);
	}
	
	public SalmonException(String message, Throwable inner) {
		super(message, inner);
	}
}
