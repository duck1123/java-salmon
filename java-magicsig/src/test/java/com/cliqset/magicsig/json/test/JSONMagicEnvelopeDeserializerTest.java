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
package com.cliqset.magicsig.json.test;

import org.junit.*;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.json.JSONMagicEnvelopeDeserializer;

public class JSONMagicEnvelopeDeserializerTest {

	@Test
	public void testDeserializeOne() {
		try {
			MagicEnvelope me = new JSONMagicEnvelopeDeserializer().deserialize(JSONMagicEnvelopeDeserializerTest.class.getResourceAsStream("/MagicEnvelopeOne.json"));
			Assert.assertEquals("RSA-SHA256", me.getAlgorithm());
			Assert.assertEquals("base64url", me.getEncoding());
			Assert.assertEquals("PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgoJPGlkPnRhZzpleGFtcGxlLmNvbSwyMDA5OmNtdC0wLjQ0Nzc1NzE4PC9pZD4KCTxhdXRob3I-CgkJPG5hbWU-dGVzdEBleGFtcGxlLmNvbTwvbmFtZT4KCQk8dXJpPmFjY3Q6dGVzdEBleGFtcGxlLmNvbTwvdXJpPgoJPC9hdXRob3I-Cgk8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-Cgk8dGl0bGU-U2FsbW9uIHN3aW0gdXBzdHJlYW0hPC90aXRsZT4KCTx1cGRhdGVkPjIwMDktMTItMThUMjA6MDQ6MDNaPC91cGRhdGVkPgo8L2VudHJ5Pg==", me.getData());
			Assert.assertEquals("application/atom+xml", me.getDataType());
			Assert.assertEquals(1, me.getSignatures().size());
			Assert.assertEquals("XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=", me.getSignatures().get(0).getKeyId());
			Assert.assertEquals("ARx-SOqs9geUJKhqgGOZ-KUE7Qe_v7w-bPrI4lPwXW95SFuvaQwtB-lhfiXltYS4PvrAEl7wXDDmd1nCR4YMag==", me.getSignatures().get(0).getValue());
		
		} catch (MagicSigException mse) {
			Assert.fail(mse.getMessage());
		}
	}
}
