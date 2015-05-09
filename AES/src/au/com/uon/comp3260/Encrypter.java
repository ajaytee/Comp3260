package au.com.uon.comp3260;

import java.nio.file.Path;

import au.com.uon.comp3260.util.Helper;

public class Encrypter {

	public Encrypter(Path inputFile, Path outputFile) {
		this(Helper.readPlainText(inputFile), Helper.readKey(inputFile),
				outputFile);
	}

	public Encrypter(String plainText, String key, Path outputFile) {

	}

}
