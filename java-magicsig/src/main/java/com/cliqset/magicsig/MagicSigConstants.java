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

import javax.xml.namespace.QName;

public class MagicSigConstants {

	public static final String MEDIA_TYPE_MAGIC_ENV_XML = "application/magic-env+xml";
	
	public static final String MEDIA_TYPE_MAGIC_ENV_JSON = "application/magic-env+json";
	
	public static final String MEDIA_TYPE_MAGIC_ENV_COMPACT = "application/magic-env";
	
	public static final String MAGIC_SIG_NS = "http://salmon-protocol.org/ns/magic-env";
	
	public static final String MAGIC_KEY_NS = "http://salmon-protocol.org/ns/magic-key";
	
	public static final String MAGIC_KEY_TYPE = "http://salmon-protocol.org/ns/magic-key";
	
	public static final QName KEY_ID_QNAME = new QName(MAGIC_KEY_NS, "key_id");
}
