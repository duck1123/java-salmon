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
package com.cliqset.magicsig.compact.test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicEnvelopeSerializer;
import com.cliqset.magicsig.MagicSigException;
import com.cliqset.magicsig.Signature;
import com.cliqset.magicsig.compact.CompactMagicEnvelopeSerializer;

public class CompactMagicEnvelopeSerializerTest {

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
			MagicEnvelopeSerializer s = new CompactMagicEnvelopeSerializer();
			s.serialize(me, baos);
			Assert.assertArrayEquals("XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=.ARx-SOqs9geUJKhqgGOZ-KUE7Qe_v7w-bPrI4lPwXW95SFuvaQwtB-lhfiXltYS4PvrAEl7wXDDmd1nCR4YMag==.PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgoJPGlkPnRhZzpleGFtcGxlLmNvbSwyMDA5OmNtdC0wLjQ0Nzc1NzE4PC9pZD4KCTxhdXRob3I-CgkJPG5hbWU-dGVzdEBleGFtcGxlLmNvbTwvbmFtZT4KCQk8dXJpPmFjY3Q6dGVzdEBleGFtcGxlLmNvbTwvdXJpPgoJPC9hdXRob3I-Cgk8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-Cgk8dGl0bGU-U2FsbW9uIHN3aW0gdXBzdHJlYW0hPC90aXRsZT4KCTx1cGRhdGVkPjIwMDktMTItMThUMjA6MDQ6MDNaPC91cGRhdGVkPgo8L2VudHJ5Pg==.YXBwbGljYXRpb24vYXRvbSt4bWw=.YmFzZTY0dXJs.UlNBLVNIQTI1Ng==".getBytes("UTF-8"), baos.toByteArray());	
		} catch (UnsupportedEncodingException uee) {
			Assert.fail("Seriously, this jvm doesn't support UTF-8?");
		} catch (MagicSigException mse) {
			Assert.fail(mse.getMessage());
		}
	}
	
	@Test
	public void testNullOutputStream() {
		try {
			new CompactMagicEnvelopeSerializer().serialize(new MagicEnvelope(), null);
			Assert.fail("Should have received a IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Cannot serialize to a null output stream.", iae.getMessage());
		} catch (Exception e) {
			Assert.fail("Expecting an IllegalArgumentException not a " + e.getClass().getName());
		}
	}
	
	@Test
	public void testNullMagicEnvelope() {
		try {
			new CompactMagicEnvelopeSerializer().serialize(null, null);
			Assert.fail("Should have received a IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			Assert.assertEquals("Cannot serialize a null magic envelope.", iae.getMessage());
		} catch (Exception e) {
			Assert.fail("Expecting an IllegalArgumentException not a " + e.getClass().getName());
		}
	}
}
