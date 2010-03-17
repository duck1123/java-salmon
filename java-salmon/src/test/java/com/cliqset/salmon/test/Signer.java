package com.cliqset.salmon.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cliqset.salmon.MagicEnvelope;
import com.cliqset.salmon.MagicKey;
import com.cliqset.salmon.MagicSigUtil;
import com.cliqset.salmon.Salmon;

public class Signer {

	private static String filename = "/DemoEntry.txt";
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				filename = args[0];
			}
			
			byte[] bytes = getBytes(filename);
			
			MagicKey key = new MagicKey(getBytes("/DemoKeys.txt"));
			MagicEnvelope env = Salmon.sign(bytes, key);
			
			FileOutputStream fos = new FileOutputStream(filename + ".env");
			fos.write(env.toBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private static byte[] getBytes(String filename) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		InputStream data = new FileInputStream(filename);
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
