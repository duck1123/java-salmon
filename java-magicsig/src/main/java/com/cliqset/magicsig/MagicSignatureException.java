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

@SuppressWarnings("serial")
public class MagicSignatureException extends Exception {

	public MagicSignatureException() {}
	
	public MagicSignatureException(String message) {
		super(message);
	}
	
	public MagicSignatureException(Throwable cause) {
		super(cause);
	}
	
	public MagicSignatureException(String message, Throwable cause) {
		super(message, cause);
	}
}
