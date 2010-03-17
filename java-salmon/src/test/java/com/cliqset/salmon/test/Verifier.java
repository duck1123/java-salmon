package com.cliqset.salmon.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cliqset.salmon.MagicEnvelope;
import com.cliqset.salmon.MagicKey;
import com.cliqset.salmon.Salmon;

public class Verifier {
	
	private static String filename = "/DemoEntry.txt.env";
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				filename = args[0];
			}
			
			byte[] bytes = getBytes(filename);
			MagicEnvelope envelope = MagicEnvelope.fromBytes(bytes);
			MagicKey key = new MagicKey(getBytes("/DemoKeys.txt"));
			List<MagicKey> keys = new ArrayList<MagicKey>();
			keys.add(key);
			System.out.println(Salmon.verify(envelope, keys));
			
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
