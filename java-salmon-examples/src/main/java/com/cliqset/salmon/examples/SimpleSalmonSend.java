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
package com.cliqset.salmon.examples;

import java.net.URI;

import com.cliqset.magicsig.MagicKey;
import com.cliqset.salmon.Salmon;

public class SimpleSalmonSend {

	public static final String keyString = "RSA.oidnySWI_e4OND41VHNtYSRzbg5SaZ0YwnQ0J1ihKFEHY49-61JFybnszkSaJJD7vBfxyVZ1lTJjxdtBJzSNGEZlzKbkFvcMdtln8g2ec6oI2G0jCsjKQtsH57uHbPY3IAkBAW3Ar14kGmOKwqoGUq1yhz93rXUomLnDYwz8E88=.AQAB.hgOzTxbqhZN9wce4I7fSKnsJu2eyzP69O9j2UZ56cuulA6_Q4YP5kaNMB53DF32L0ASqHBCM1WXz984hptlT0e4U3asXxqegTqrGPNAXw5A6r2E-9MeS84LDFUnUz420YPxMxknzMJBeAz21PuKyrv_QZf6zmRQ0m5eQ0QNJoYE=";
	public static final String entry = 
	"<entry xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\">" +
		"<id>tag:somesite.com,2010-11-05:/username/entry/7k23hk0hjiAlIFjkggmCTjPObFoj5XIbpWrs5iCokMY</id>" +
		"<published>2010-11-05T19:38:27.703Z</published>" +
		"<updated>2010-11-05T19:38:27.703Z</updated>" +
		"<summary type=\"html\">hey, so here is the summary</summary>" + 
		"<title type=\"text\">hey, so here is the title</title>" +
		"<author>" +
			"<name>Charlie</name>" +
			"<uri>acct:charlie@domian.com</uri>" +
		"</author>" +
	"</entry>";
	public static final URI destinationUser = URI.create("acct:charlie@reticulateme.appspot.com");
	
	public static void main(String[] args) {
		try {
			Salmon salmon = Salmon.getDefault();
			MagicKey key = new MagicKey(keyString.getBytes("UTF-8"));
			salmon.signAndDeliver(entry.getBytes("UTF-8"), key, destinationUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
