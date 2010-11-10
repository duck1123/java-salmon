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

import org.junit.Test;

import org.junit.*;

import com.cliqset.magicsig.MagicKey;

public class MagicKeyTest extends BaseTestCase {
	@Test
	public void testGetComponents() {
		String type = "RSA";
		byte[] nBytes = new byte[] {-103, 88, 24, -15, 19, 122, 81, 16, 83, -78, -39, -35, -66, 101, 20, 61, -66, 20, 101, 55, 112, -65, 9, -99, 117, 34, -124, -25, 63, -29, -68, -91, 4, 43, -84, -92, -42, -19, -21, -84, 47, 114, 55, -55, 58, 22, 41, 70, -113, -46, -76, 121, -27, 15, 123, 104, 95, -32, -8, -2, -28, 41, -121, -61};
		byte[] eBytes = new byte[] {1, 0, 1};
		byte[] dBytes = new byte[] {46, 12, -65, -56, -67, -31, -80, -80, 103, -126, 65, 93, 15, 13, 73, -53, -44, -26, 73, 19, 34, 31, -84, -94, -123, -121, -83, 67, -56, -14, -6, 54, 87, 118, -58, 87, 119, -59, 121, -71, -67, -28, -72, 17, -57, -109, -125, 55, -10, 45, -45, 118, -30, 36, 114, -72, -36, -78, 37, -112, 111, -66, -61, -15};
		MagicKey key = new MagicKey(getBytes("/BasicRSAKey.txt"));
		Assert.assertArrayEquals(nBytes, key.getN());
		Assert.assertArrayEquals(eBytes, key.getE());
		Assert.assertArrayEquals(dBytes, key.getD());
		Assert.assertEquals(type, key.getType());
	}
/*	
	@Test
	public void testGetKeyHash() {
		String keyHash = "XV4QrdLUj8SyFr-hhobmeKlOTSTg6Rd4sbQXHnx4ejg=";
		MagicKey key = new MagicKey(getBytes("/BasicRSAKey.txt"));
		Assert.assertEquals(keyHash, key.getKeyId());
	}

	@Test
	public void testToStringDefault() {
		String publicKeyString = "RSA.mVgY8RN6URBTstndvmUUPb4UZTdwvwmddSKE5z_jvKUEK6yk1u3rrC9yN8k6FilGj9K0eeUPe2hf4Pj-5CmHww==.AQAB";
		MagicKey key = new MagicKey(getBytes("/BasicRSAKey.txt"));
		Assert.assertEquals(publicKeyString, key.toString());
	}
	@Test
	public void testToStringFull() {
		String fullKeyString = "RSA.mVgY8RN6URBTstndvmUUPb4UZTdwvwmddSKE5z_jvKUEK6yk1u3rrC9yN8k6FilGj9K0eeUPe2hf4Pj-5CmHww==.AQAB.Lgy_yL3hsLBngkFdDw1Jy9TmSRMiH6yihYetQ8jy-jZXdsZXd8V5ub3kuBHHk4M39i3TduIkcrjcsiWQb77D8Q==";
		MagicKey key = new MagicKey(getBytes("/BasicRSAKey.txt"));
		Assert.assertEquals(fullKeyString, key.toString(true));
	}
*/
}
