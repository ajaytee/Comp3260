package au.com.uon.comp3260.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class writes the output file
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class OutputFileWriter {

	private String title;

	private String plainText;
	private String key;
	private String cipherText;
	private long startTime;
	private long endTime;

	public void writeToFile(Path outputFile) {
		long duration = endTime - startTime;

		List<String> lines = new ArrayList<String>();
		lines.add(title);
		lines.add("Plaintext P: " + plainText);
		lines.add("Key K: " + key);
		lines.add("Ciphertext C: " + cipherText);
		lines.add("Running time: " + duration + "ms");
		lines.add("Avalanche:");
		lines.add("TODO, rest not implemented :)");

		try {
			Files.write(outputFile, lines);
		} catch (IOException e) {
			System.err.println("Could not write output file");
			e.printStackTrace();
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
