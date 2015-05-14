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

	/**
	 * True = decryption False = encryption
	 */
	private boolean decryption;

	private String plainText;
	private String key;
	private String cipherText;
	private long startTime;
	private long endTime;
	private int aes0[];
	private int aes1[];
	private int aes2[];
	private int aes3[];
	private int aes4[];
	private final Path outputFile;

	public OutputFileWriter(Path outputFile) {
		// sample data to be removed!
		aes0 = new int[11];
		aes1 = new int[11];
		aes2 = new int[11];
		aes3 = new int[11];
		aes4 = new int[11];
		for (int i = 0; i < 11; i++) {
			aes0[i] = 40 + i;
			aes1[i] = 50 + i;
			aes2[i] = 60 + i;
			aes3[i] = 70 + i;
			aes4[i] = 80 + i;
		}
		this.outputFile = outputFile;
	}

	public void writeToFile() {
		long duration = endTime - startTime;

		List<String> lines = new ArrayList<String>();
		if (decryption) {
			lines.add("DECRYPTION");
		} else {
			lines.add("ENCRYPTION");
		}
		lines.add("Plaintext P: " + plainText);
		lines.add("Key K: " + key);
		lines.add("Ciphertext C: " + cipherText);
		lines.add("Running time: " + duration + "ms");
		lines.add("Avalanche:");
		lines.add("P and Pi under K\nRound AES0 AES1 AES2 AES3 AES4");
		for (int round = 0; round < 11; round++) {
			String roundString = round + "    ";
			if (round > 9) {
				roundString = round + "   ";
			}
			lines.add(String.format("%s %d   %d   %d   %d   %d", roundString,
					aes0[round], aes1[round], aes2[round], aes3[round],
					aes4[round]));
		}
		lines.add("\nP under K and Ki\nRound AES0 AES1 AES2 AES3 AES4");
		for (int round = 0; round < 11; round++) {
			String roundString = round + "    ";
			if (round > 9) {
				roundString = round + "   ";
			}
			lines.add(String.format("%s %d   %d   %d   %d   %d", roundString,
					aes0[round], aes1[round], aes2[round], aes3[round],
					aes4[round]));
		}

		try {
			Files.write(outputFile, lines);
		} catch (IOException e) {
			System.err.println("Could not write output file");
			e.printStackTrace();
		}
	}

	public void setDecryption(boolean decryption) {
		this.decryption = decryption;
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

	public void setAvalancheDataAes0(int[] aes0) {
		if (aes0.length != 11) {
			throw new RuntimeException(
					"Avalanche Data must have 11 data entries");
		}
		this.aes0 = aes0;
	}

	public void setAvalancheDataAes1(int[] aes1) {
		if (aes1.length != 11) {
			throw new RuntimeException(
					"Avalanche Data must have 11 data entries");
		}
		this.aes1 = aes1;
	}

	public void setAvalancheDataAes2(int[] aes2) {
		if (aes2.length != 11) {
			throw new RuntimeException(
					"Avalanche Data must have 11 data entries");
		}
		this.aes2 = aes2;
	}

	public void setAvalancheDataAes3(int[] aes3) {
		if (aes3.length != 11) {
			throw new RuntimeException(
					"Avalanche Data must have 11 data entries");
		}
		this.aes3 = aes3;
	}

	public void setAvalancheDataAes4(int[] aes4) {
		if (aes4.length != 11) {
			throw new RuntimeException(
					"Avalanche Data must have 11 data entries");
		}
		this.aes4 = aes4;
	}

}
