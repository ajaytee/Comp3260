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

	public Encrypter(Path inputFile, Path outputFile) {
		this(Helper.readPlainText(inputFile), Helper.readKey(inputFile),
				outputFile);
	}

	public Encrypter(String plainText, String key, Path outputFile) {
		long start = System.currentTimeMillis();
		byte[] plainTextByteArray = Helper.stringToByteArray(plainText);

		System.out.println("Input Bytes:");
		System.out.println(Helper.matrixToString(
				Helper.arrayToMatrix(plainTextByteArray), true));
		byte[] keyBytes = Helper.stringToByteArray(key);
		byte[][] encrypted = encrypt(plainTextByteArray, keyBytes, AESType.AES0);
		long end = System.currentTimeMillis();
		System.out.println("Encrypted Bytes:");
		System.out.println(Helper.matrixToString(encrypted, true));
		OutputFileWriter writer = new OutputFileWriter();
		writer.setKey(key);
		writer.setPlainText(plainText);
		writer.setTitle("ENCRYPTION");
		writer.setStartTime(start);
		writer.setEndTime(end);
		writer.setCipherText(Helper.arrayToString(
				Helper.matrixToArray(encrypted), false));
		writer.writeToFile(outputFile);
	}

	private byte[][] encrypt(byte[] inputArray, byte[] key, AESType type) {
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
				// TODO: ANDREW
				// output = columnMixer.
			}
			if (!type.isSkipAddRoundKey()) {
				output = roundKeyAdder.addRoundKey(output, key);
			}
			// TODO Calculate Avalange Effect and add data to output file writer
		}
		return output;
	}
}
