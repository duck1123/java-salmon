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

import java.util.LinkedList;
import java.util.List;

public class EnvelopeVerificationResult {

	private byte[] data = null;
	
	private List<SignatureVerificationResult> signatureVerificationResults = null;

	public EnvelopeVerificationResult withData(byte[] value) {
		this.data = value;
		return this;
	}
	
	public EnvelopeVerificationResult withSignatureVerificationResult(SignatureVerificationResult value) {
		if (null == this.signatureVerificationResults) {
			this.signatureVerificationResults = new LinkedList<SignatureVerificationResult>();
		}
		this.signatureVerificationResults.add(value);
		return this;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setSignatureVerificationResults(
			List<SignatureVerificationResult> signatureVerificationResults) {
		this.signatureVerificationResults = signatureVerificationResults;
	}

	public List<SignatureVerificationResult> getSignatureVerificationResults() {
		return signatureVerificationResults;
	}
}
