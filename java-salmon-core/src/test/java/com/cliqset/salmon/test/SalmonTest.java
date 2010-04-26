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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.junit.Assert;
import com.cliqset.salmon.MagicEnvelope;
import com.cliqset.salmon.MagicSigUtil;

public class SalmonTest {
/*
	@Test
	public void testFetchDataURIKey() {
		try {
			MagicKey key = Salmon.fetchKey(URI.create("data:application/magic-public-key;,RSA.AL2zYG89oU1spKW_vTt5jeAkiOGrxJr4ivEzmj5rp8TXr6OySR97vMb7iPBCRlnHoYvQ74gFMA05e_3rCi3AGap6myqYYq3PsUcAzUTufge-m3YRPYWIkB3S-ioAFVAo8vkK4RYrzrYyPfbTbSuRn3bgpfTFe0wcMaGPuZ_homNd.AQAB"));
			Assert.assertEquals("RSA", key.getType());
			Assert.assertArrayEquals(MagicSigUtil.decode("AL2zYG89oU1spKW_vTt5jeAkiOGrxJr4ivEzmj5rp8TXr6OySR97vMb7iPBCRlnHoYvQ74gFMA05e_3rCi3AGap6myqYYq3PsUcAzUTufge-m3YRPYWIkB3S-ioAFVAo8vkK4RYrzrYyPfbTbSuRn3bgpfTFe0wcMaGPuZ_homNd"), key.getN());
			Assert.assertArrayEquals(MagicSigUtil.decode("AQAB"), key.getE());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testFetchDataURIKeyTwo() {
		try {
			MagicKey key = Salmon.fetchKey(URI.create("data:application/magic-public-key;,RSA.XxeMlhQWq-NQgYBfLv1cuw2YapWpFMwU_nZ5yfUL-jrnZZZiuYrHXzpVnwd_9VJiSfZZY_zuXz571XE3IyX7wg==.AQAB"));
			Assert.assertEquals("RSA", key.getType());
			Assert.assertArrayEquals(MagicSigUtil.decode("XxeMlhQWq-NQgYBfLv1cuw2YapWpFMwU_nZ5yfUL-jrnZZZiuYrHXzpVnwd_9VJiSfZZY_zuXz571XE3IyX7wg=="), key.getN());
			Assert.assertArrayEquals(MagicSigUtil.decode("AQAB"), key.getE());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testparseEntry() {
		try {
			String encodedEntry = "PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnPz4KPGVudHJ5IHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDA1L0F0b20nPgogIDxpZD50YWc6ZXhhbXBsZS5jb20sMjAwOTpjbXQtMC40NDc3NTcxODwvaWQ-ICAKICA8YXV0aG9yPjxuYW1lPnRlc3RAZXhhbXBsZS5jb208L25hbWU-PHVyaT5hY2N0OmNoYXJsaWVAY2xpcXNldC5jb208L3VyaT48L2F1dGhvcj4KICA8dGhyOmluLXJlcGx5LXRvIHhtbG5zOnRocj0naHR0cDovL3B1cmwub3JnL3N5bmRpY2F0aW9uL3RocmVhZC8xLjAnCiAgICAgIHJlZj0ndGFnOmJsb2dnZXIuY29tLDE5OTk6YmxvZy04OTM1OTEzNzQzMTMzMTI3MzcucG9zdC0zODYxNjYzMjU4NTM4ODU3OTU0Jz50YWc6YmxvZ2dlci5jb20sMTk5OTpibG9nLTg5MzU5MTM3NDMxMzMxMjczNy5wb3N0LTM4NjE2NjMyNTg1Mzg4NTc5NTQKICA8L3Rocjppbi1yZXBseS10bz4KICA8Y29udGVudD5TYWxtb24gc3dpbSB1cHN0cmVhbSE8L2NvbnRlbnQ-CiAgPHRpdGxlPlNhbG1vbiBzd2ltIHVwc3RyZWFtITwvdGl0bGU-CiAgPHVwZGF0ZWQ-MjAwOS0xMi0xOFQyMDowNDowM1o8L3VwZGF0ZWQ-CjwvZW50cnk-CiAgICA=";
			byte[] decoded = MagicSigUtil.decode(encodedEntry);
			Entry e = Salmon.parseEntry(decoded);
			Person author = e.getAuthor();
			IRI uri = author.getUri();
			Assert.assertEquals("acct:charlie@cliqset.com", uri.toString());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	*/
	@Test
	public void testDecodeDataOne() {
		try {
			MagicEnvelope envelope = MagicEnvelope.fromBytes(getBytes("/entry.txt.env"));
			Assert.assertArrayEquals(MagicSigUtil.decode(envelope.getData().getValue()), getBytes("/entry.txt"));
		} catch (Exception e) {
			Assert.fail();
		}
	}
	

	private byte[] getBytes(String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = MagicSigUtilTest.class.getResourceAsStream(filename);
		try {
			int i;
			while (-1 != (i = data.read(buffer, 0, 512))) {
				baos.write(buffer, 0, i);
			}
		} catch (IOException e) {
			
		}
		return baos.toByteArray();
	}
	
	private String getString(String filename, String encoding) {
		try {
			return new String(getBytes(filename), encoding);
		} catch (Exception e) {
			return "";
		}
	}
}
