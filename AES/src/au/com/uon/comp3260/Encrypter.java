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
		byte[][] encrypted = encryptByteArray(plainTextByteArray, keyBytes,
				type);
		System.out.println("Encrypted Bytes:");
		System.out.println(Helper.matrixToString(encrypted, true));

		// Calculate avalanche effect
		String encryptedText = Helper.matrixToString(encrypted, false);
		int[] roundAverages = avalanche(plainText, encryptedText, key);
		int plainTextAverage = roundAverages[0];
		int keyAverage = roundAverages[1];
		// TODO: add avalanche averages to output

		String cipherText = Helper.arrayToString(
				Helper.matrixToArray(encrypted), false);
		return cipherText;
	}

	public byte[][] encryptByteArray(byte[] inputArray, byte[] key, AESType type) {
		byte[][] input = Helper.arrayToMatrix(inputArray);
		AddRoundKey roundKeyAdder = new AddRoundKey();
		SubstituteBytes byteSubstituter = new SubstituteBytes();
		ShiftRows rowShifter = new ShiftRows();
		MixColumns columnMixer = new MixColumns();

		byte[][] output;
		output = roundKeyAdder.addRoundKey(input, key);
		for (int round = 0; round < 10; round++) {
			if (!type.isSkipSubBytes()) {
				output = byteSubstituter.substituteBytes(output, false);
			}
			if (!type.isSkipShiftRows()) {
				output = rowShifter.shiftRows(output, false);
			}
			if (!type.isSkipMixColumns()) {
				output = columnMixer.mixColumns(output, false);
			}
			if (!type.isSkipAddRoundKey()) {
				output = roundKeyAdder.addRoundKey(output, key);
			}
			// TODO Calculate Avalange Effect and add data to output file writer

		}
		return output;
	}

	private int[] avalanche(String plainText, String encryptedText, String key) {
		int plainTextChanges = 0;
		int keyChanges = 0;

		// change bits of plain text and key one at a time
		String oneChar = "1";
		for (int i = 0; i < plainText.length(); i++) {
			StringBuilder newString = new StringBuilder(plainText);
			StringBuilder newKey = new StringBuilder(key);
			if (plainText.charAt(i) == oneChar.charAt(0)) { // if char is a 1
				newString.setCharAt(i, '0'); // change to 0
			} else {
				newString.setCharAt(i, '1'); // if 0, change to 1
			}
			if (key.charAt(i) == oneChar.charAt(0)) {
				newKey.setCharAt(i, '0');
			} else {
				newKey.setCharAt(i, '1');
			}

			// encrypt the newString with key
			// compare the new encrypted string with encryptedText, add count of
			// changes to plainTextChanges

			// encrypt plainText with newKey
			// compare the new encrypted string with encryptedText, add count of
			// changes to keyChanges

		}
		int plainTextAvg = plainTextChanges / plainText.length();
		int keyAvg = keyChanges / plainText.length();
		return new int[] { plainTextAvg, keyAvg };
	}
}
