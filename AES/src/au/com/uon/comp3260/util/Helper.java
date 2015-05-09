package au.com.uon.comp3260.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Helper {

	public static String readLine(Path inputFile, int lineNbr) {
		try {
			List<String> lines = Files.readAllLines(inputFile);
			if (lines != null && lines.size() > lineNbr) {
				return lines.get(lineNbr).replaceAll(" ", "");
			}
		} catch (IOException e) {
			System.err.println("Could not read key from text");
			e.printStackTrace();
		}
		return null;
	}

	public static String readPlainText(Path inputFile) {
		return readLine(inputFile, 0);
	}

	public static String readCipherText(Path inputFile) {
		return readLine(inputFile, 0);
	}

	public static String readKey(Path inputFile) {
		return readLine(inputFile, 1);
	}

}
