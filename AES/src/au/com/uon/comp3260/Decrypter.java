package au.com.uon.comp3260;

import java.nio.file.Path;

import au.com.uon.comp3260.util.Helper;

public class Decrypter {
	public Decrypter(Path inputFile, Path outputFile) {
		this(Helper.readCipherText(inputFile), Helper.readKey(inputFile),
				outputFile);
	}

	public Decrypter(String cipherText, String key, Path outputFile) {

	}
}
