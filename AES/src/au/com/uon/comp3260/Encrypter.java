package au.com.uon.comp3260;

import java.nio.file.Path;

import au.com.uon.comp3260.util.Helper;
import au.com.uon.comp3260.util.OutputFileWriter;

/**
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class Encrypter {

	public void encrypt(Path inputFile, Path outputFile) {
		String plainText = Helper.readPlainText(inputFile);
		String key = Helper.readKey(inputFile);
		OutputFileWriter writer = null;
		if (outputFile != null) {
			writer = new OutputFileWriter(outputFile);
			writer.setKey(key);
			writer.setPlainText(plainText);
			writer.setDecryption(false);
			writer.setStartTime(System.currentTimeMillis());
		}
		for (AESType type : AESType.values()) {
			String cipherText = encrypt(plainText, key, writer, type);
			if (type == AESType.AES0 && writer != null) {
				writer.setCipherText(cipherText);
			}
		}
		if (writer != null) {
			writer.setEndTime(System.currentTimeMillis());
			writer.writeToFile();
		}
	}

	public String encrypt(String plainText, String key,
			OutputFileWriter writer, AESType type) {

		byte[] plainTextByteArray = Helper.stringToByteArray(plainText);

		System.out.println("Input Bytes:");
		System.out.println(Helper.matrixToString(
				Helper.arrayToMatrix(plainTextByteArray), true));
		byte[] keyBytes = Helper.stringToByteArray(key);
		byte[][][] encrypted = encryptByteArray(plainTextByteArray, keyBytes,
				type);
		System.out.println("Encrypted Bytes:");
		System.out.println(Helper.matrixToString(encrypted[10], true));

		// Calculate avalanche effect
		String encryptedText[] = new String[11];
		for (int round = 0; round < 11; round++) {
			encryptedText[round] = Helper.matrixToString(encrypted[round],
					false);
		}
		int[][] roundAverages = avalanche(plainText, encryptedText, key, type);

		switch (type) {
		case AES0:
			writer.setAvalancheDataAes0(roundAverages);
			break;
		case AES1:
			writer.setAvalancheDataAes1(roundAverages);
			break;
		case AES2:
			writer.setAvalancheDataAes2(roundAverages);
			break;
		case AES3:
			writer.setAvalancheDataAes3(roundAverages);
			break;
		case AES4:
			writer.setAvalancheDataAes4(roundAverages);
			break;
		default:
			break;
		}

		String cipherText = Helper.arrayToString(
				Helper.matrixToArray(encrypted[10]), false);
		return cipherText;
	}

	public byte[][][] encryptByteArray(byte[] inputArray, byte[] key,
			AESType type) {
		byte[][] input = Helper.arrayToMatrix(inputArray);
		AddRoundKey roundKeyAdder = new AddRoundKey(key, false);
		SubstituteBytes byteSubstituter = new SubstituteBytes();
		ShiftRows rowShifter = new ShiftRows();
		MixColumns columnMixer = new MixColumns();

		// 1. round, 2. row, 3. column
		byte[][][] output = new byte[11][input.length][input[0].length];
		output[0] = roundKeyAdder.addRoundKey(input, 0, false);
		for (int round = 1; round < 11; round++) {
			output[round] = output[round - 1];
			if (!type.isSkipSubBytes()) {
				output[round] = byteSubstituter.substituteBytes(output[round],
						false);
			}
			if (!type.isSkipShiftRows()) {
				output[round] = rowShifter.shiftRows(output[round], false);
			}
			if (!type.isSkipMixColumns()) {
				output[round] = columnMixer.mixColumns(output[round], false);
			}
			if (!type.isSkipAddRoundKey()) {
				output[round] = roundKeyAdder.addRoundKey(output[round], round,
						false);
			}
		}
		return output;
	}

	// 1 = 0 --> plaintext; 1=1 --> key
	// 2. round;
	private int[][] avalanche(String plainText, String[] encryptedText,
			String key, AESType type) {
		int plainTextChanges[] = new int[11];
		int keyChanges[] = new int[11];

		// change bits of plain text and key one at a time
		String oneChar = "1";
		for (int i = 0; i < plainText.length(); i++) {
			StringBuilder sb1 = new StringBuilder(plainText);
			StringBuilder sb2 = new StringBuilder(key);
			if (plainText.charAt(i) == oneChar.charAt(0)) { // if char is a 1
				sb1.setCharAt(i, '0'); // change to 0
			} else {
				sb1.setCharAt(i, '1'); // if 0, change to 1
			}
			if (key.charAt(i) == oneChar.charAt(0)) {
				sb2.setCharAt(i, '0');
			} else {
				sb2.setCharAt(i, '1');
			}
			String newString = sb1.toString();
			String newKey = sb2.toString();

			// change Strings to byte arrays
			byte[] textBytes = Helper.stringToByteArray(plainText);
			byte[] keyBytes = Helper.stringToByteArray(key);
			byte[] changedTextBytes = Helper.stringToByteArray(newString);
			byte[] changedKeyBytes = Helper.stringToByteArray(newKey);

			// encrypt the new byte arrays
			byte[][][] newTextEncrypted = encryptByteArray(changedTextBytes,
					keyBytes, type);
			byte[][][] newKeyEncrypted = encryptByteArray(textBytes,
					changedKeyBytes, type);

			for (int round = 0; round < 11; round++) {
				// change matrix to Strings
				String newTextEncryption = Helper.matrixToString(
						newTextEncrypted[round], false);
				String newKeyEncryption = Helper.matrixToString(
						newKeyEncrypted[round], false);

				System.out.println("\n\n Avalanche for " + type.getName()
						+ " Round: " + round + ":\n" + newTextEncryption
						+ "\n Original:\n" + encryptedText[round]);
				// compare the new encrypted strings with encryptedText, add
				// count
				// of
				// changes to plainTextChanges and keyChanges
				for (int j = 0; j < plainText.length(); j++) {
					if (newTextEncryption.charAt(j) != encryptedText[round]
							.charAt(j)) {
						plainTextChanges[round]++;
					}
					if (newKeyEncryption.charAt(j) != encryptedText[round]
							.charAt(j)) {
						keyChanges[round]++;
					}
				}
			}
		}

		int plainTextAvg[] = new int[11];
		int keyAvg[] = new int[11];
		int textLength = plainText.length();
		for (int round = 0; round < 11; round++) {
			plainTextAvg[round] = plainTextChanges[round] / textLength;
			keyAvg[round] = keyChanges[round] / textLength;
		}
		return new int[][] { plainTextAvg, keyAvg };
	}
}
