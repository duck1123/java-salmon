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

public class SignatureVerificationResult {

	private Signature signature;
	private Key key;
	
	public SignatureVerificationResult withSignature(Signature signature) {
		this.signature = signature;
		return this;
	}
	
	public SignatureVerificationResult withKey(Key key) {
		this.key = key;
		return this;
	}
	
	public boolean isVerified() {
		return null != getKey();
	}

	public void setSignature(Signature sig) {
		this.signature = sig;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}
}
