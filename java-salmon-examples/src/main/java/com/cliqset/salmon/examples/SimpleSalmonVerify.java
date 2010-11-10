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

import java.io.ByteArrayInputStream;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.salmon.Salmon;

public class SimpleSalmonVerify {

	public static final String envelope = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><env xmlns=\"http://salmon-protocol.org/ns/magic-env\"><data type=\"application/atom+xml\">PGVudHJ5IHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20iPg0KCTxpZD50YWc6cmV0aWN1bGF0ZW1lLmFwcHNwb3QuY29tLDIwMTAtMTEtMDY6L3NhbG1vbmxpYi9lbnRyeS8wQTBHZFE2cmJpYjdRZ3RfZjE5UTdiUE5hRWVxUVh6NXhNb2FfamN1Ti1BPC9pZD4NCgk8cHVibGlzaGVkPjIwMTAtMTEtMDZUMDE6NDY6NTEuMTQ3WjwvcHVibGlzaGVkPg0KCTx1cGRhdGVkPjIwMTAtMTEtMDZUMDE6NDY6NTEuMTQ3WjwvdXBkYXRlZD4NCgk8c3VtbWFyeSB0eXBlPSJodG1sIj5oZXksIHNvIGhlcmUgaXMgdGhlIHN1bW1hcnk8L3N1bW1hcnk-DQoJPHRpdGxlIHR5cGU9InRleHQiPmhleSwgc28gaGVyZSBpcyB0aGUgdGl0bGU8L3RpdGxlPg0KCTxsaW5rIGhyZWY9Imh0dHA6Ly9yZXRpY3VsYXRlbWUuYXBwc3BvdC5jb20vc2FsbW9ubGliL2VudHJ5LzBBMEdkUTZyYmliN1FndF9mMTlRN2JQTmFFZXFRWHo1eE1vYV9qY3VOLUEiIHR5cGU9InRleHQveGh0bWwiIHJlbD0iYWx0ZXJuYXRlIi8-DQoJPGF1dGhvcj4NCgkJPG5hbWU-U2FsbW9uIExpYnJhcnk8L25hbWU-DQoJCTx1cmk-YWNjdDpzYWxtb25saWJAcmV0aWN1bGF0ZW1lLmFwcHNwb3QuY29tPC91cmk-DQoJPC9hdXRob3I-DQo8L2VudHJ5Pg0KCQkJ</data><encoding>base64url</encoding><alg>RSA-SHA256</alg><sig>psinLK6mpn8IPrKRpta06m49dr2XggN6Bjkbnp3wLwEHClmgwBkwk4Q-3BGbEFxsCR0ogCiTj5JKZbkeR3IkK9bKlEYjMAXWLlrBkDKhfyOitdTbqcCREnd9tRqh562kCF84JY3m1NxPCU1MovMq0zUqryVytAZmgQoEPdzy3Ug=</sig></env>";
	
	public static void main(String[] args) {
		try {
			Salmon salmon = Salmon.getDefault();
			MagicEnvelope m = MagicEnvelope.fromInputStream("application/magic-env+xml", new ByteArrayInputStream(envelope.getBytes("UTF-8")));
			byte[] verifiedData = salmon.verify(m);
			System.out.print(new String(verifiedData, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
