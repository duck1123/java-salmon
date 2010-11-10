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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;
import com.cliqset.magicsig.json.JSONMagicEnvelopeSerializer;

public class JSONMagicEnvelopeSerializerTest {

	@Test
	public void testSuccessfulSerializationOne() {
		try {
			MagicEnvelope me = new MagicEnvelope()
									.withAlgorithm("RSA-SHA256")
									.withEncoding("base64url")
									.withDataType("application/atom+xml")
									.withData("PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgoJPGlkPnRhZzpleGFtcGxlLmNvbSwyMDA5OmNtdC0wLjQ0Nzc1NzE4PC9pZD4KCTxhdXRob3I-CgkJPG5hbWU-dGVzdEBleGFtcGxlLmNvbTwvbmFtZT4KCQk8dXJpPmFjY3Q6dGVzdEBleGFtcGxlLmNvbTwvdXJpPgoJPC9hdXRob3I-Cgk8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-Cgk8dGl0bGU-U2FsbW9uIHN3aW0gdXBzdHJlYW0hPC90aXRsZT4KCTx1cGRhdGVkPjIwMDktMTItMThUMjA6MDQ6MDNaPC91cGRhdGVkPgo8L2VudHJ5Pg==")
									.withSignature(new Signature()
											.withKeyId("XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=")
											.withValue("ARx-SOqs9geUJKhqgGOZ-KUE7Qe_v7w-bPrI4lPwXW95SFuvaQwtB-lhfiXltYS4PvrAEl7wXDDmd1nCR4YMag=="));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			MagicEnvelopeSerializer s = new JSONMagicEnvelopeSerializer();
			s.serialize(me, baos);
			Assert.assertEquals(new String(getBytes("/MagicEnvelopeOne.json"), "UTF-8"), baos.toString("UTF-8"));	
		} catch (MagicSigException mse) {
			Assert.fail(mse.getMessage());
		} catch (UnsupportedEncodingException e) {
			Assert.fail("Not UTF-8?");
		}
	}
	
	public static byte[] getBytes(String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = JSONMagicEnvelopeSerializerTest.class.getResourceAsStream(filename);
		try {
			int i;
			while (-1 != (i = data.read(buffer, 0, 512))) {
				baos.write(buffer, 0, i);
			}
		} catch (IOException e) {
			
		}
		return baos.toByteArray();
	}
}
