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

package com.cliqset.salmon.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import com.cliqset.magicsig.MagicEnvelope;
import com.cliqset.magicsig.MagicKey;
import com.cliqset.salmon.DataParser;
import com.cliqset.salmon.KeyFinder;
import com.cliqset.salmon.Salmon;
import com.cliqset.salmon.SalmonException;

public class SalmonTest extends BaseTestCase {
	/*
	@Test
	public void testSignBasic() {
		try {
			Salmon s = new Salmon()
				.withDataParser(new BasicAtomDataParser())
				.withKeyFinder(new BasicKeyFinder());
			MagicEnvelope env = s.sign(getBytes("/BasicAtom.txt"), new MagicKey(getBytes("/BasicRSAKey.txt")));
			Assert.assertEquals(env.getAlg(), "RSA-SHA256");
			Assert.assertEquals(env.getData().getValue(), "PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgoJPGlkPnRhZzpleGFtcGxlLmNvbSwyMDA5OmNtdC0wLjQ0Nzc1NzE4PC9pZD4KCTxhdXRob3I-CgkJPG5hbWU-dGVzdEBleGFtcGxlLmNvbTwvbmFtZT4KCQk8dXJpPmFjY3Q6dGVzdEBleGFtcGxlLmNvbTwvdXJpPgoJPC9hdXRob3I-Cgk8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-Cgk8dGl0bGU-U2FsbW9uIHN3aW0gdXBzdHJlYW0hPC90aXRsZT4KCTx1cGRhdGVkPjIwMDktMTItMThUMjA6MDQ6MDNaPC91cGRhdGVkPgo8L2VudHJ5Pg==");
			Assert.assertEquals(env.getData().getType(), "application/atom+xml");
			Assert.assertEquals(env.getEncoding(), "base64url");
			//Assert.assertEquals(env.getSigs().get(0).getKeyId(), "XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=");
			Assert.assertEquals(env.getSigs().get(0).getValue(), "ARx-SOqs9geUJKhqgGOZ-KUE7Qe_v7w-bPrI4lPwXW95SFuvaQwtB-lhfiXltYS4PvrAEl7wXDDmd1nCR4YMag==");
			
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
	*/
	@Test
	public void testVerifyBasic() {
		try {
			Salmon s = new Salmon()
				.withDataParser(new BasicAtomDataParser())
				.withKeyFinder(new BasicKeyFinder());
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			
			byte[] dataBytes = s.verify(env);
			Assert.assertArrayEquals(dataBytes, getBytes("/BasicAtom.txt"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testEmptyDataParsers() {
		Salmon s = new Salmon()
			.withKeyFinder(new BasicKeyFinder());
		try {
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			byte[] dataBytes = s.verify(env);
			Assert.fail("Should get a SalmonException.");
		} catch (SalmonException se) {
			Assert.assertEquals("Unable to extract signer URI from data.", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a SalmonException");
		}
	}
	
	@Test
	public void testNoMatchingDataParser() {
		Salmon s = new Salmon()
			.withDataParser(new DataParser() {

				public URI getSignerUri(byte[] data) throws SalmonException {
					return URI.create("acct:test@example.com");
				}

				public boolean parsesMimeType(String mimeType) {
					return false;
				}
			});
		try {
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			byte[] dataBytes = s.verify(env);
			Assert.fail("Should get a SalmonException.");
		} catch (SalmonException se) {
			Assert.assertEquals("Unable to extract signer URI from data.", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a SalmonException");
		}
	}
	
	@Test
	public void testEmptyKeyFinder() {
		Salmon s = new Salmon()
			.withDataParser(new BasicAtomDataParser());
		try {
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			byte[] dataBytes = s.verify(env);
			Assert.fail("Should get a SalmonException.");
		} catch (SalmonException se) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@example.com", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a SalmonException");
		}
	}
	
	@Test
	public void testEmptyListKeyFinder() {
		Salmon s = new Salmon()
			.withDataParser(new BasicAtomDataParser())
			.withKeyFinder(new KeyFinder() {

				public List<MagicKey> findKeys(URI signerUri) throws SalmonException {
					return new ArrayList<MagicKey>();
				}
			});
		try {
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			byte[] dataBytes = s.verify(env);
			Assert.fail("Should get a SalmonException.");
		} catch (SalmonException se) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@example.com", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a SalmonException");
		}
	}
	
	@Test
	public void testNullKeyFinder() {
		Salmon s = new Salmon()
			.withDataParser(new BasicAtomDataParser())
			.withKeyFinder(new KeyFinder() {

				public List<MagicKey> findKeys(URI signerUri) throws SalmonException {
					return null;
				}

			});
		try {
			MagicEnvelope env = MagicEnvelope.fromBytes(getBytes("/BasicEnvelope.txt"));
			byte[] dataBytes = s.verify(env);
			Assert.fail("Should get a SalmonException.");
		} catch (SalmonException se) {
			Assert.assertEquals("Unable to find keys for signer: acct:test@example.com", se.getMessage());
		} catch (Exception e) {
			Assert.fail("Should get a SalmonException");
		}
	}
	
	
	public class BasicAtomDataParser implements DataParser {

		public URI getSignerUri(byte[] data) throws SalmonException {
			return URI.create("acct:test@example.com");
		}

		public boolean parsesMimeType(String mimeType) {
			return "application/atom+xml".equals(mimeType);
		}
	}
	
	public class BasicKeyFinder implements KeyFinder {

		public List<MagicKey> findKeys(URI signerUri) throws SalmonException {
			if (URI.create("acct:test@example.com").equals(signerUri)) {
				return Arrays.asList(new MagicKey[] { new MagicKey(getBytes("/BasicRSAKey.txt"))});
			}
			throw new SalmonException("Can't find keys for " + signerUri);
		}	
	}
}
