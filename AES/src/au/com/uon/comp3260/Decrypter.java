package au.com.uon.comp3260;

import java.nio.file.Path;

import au.com.uon.comp3260.util.Helper;
import au.com.uon.comp3260.util.OutputFileWriter;

/**
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class Decrypter {
	public Decrypter(Path inputFile, Path outputFile) {
		this(Helper.readCipherText(inputFile), Helper.readKey(inputFile),
				outputFile);
	}

	public Decrypter(String cipherText, String key, Path outputFile) {
		long start = System.currentTimeMillis();
		byte[] cipherTextByteArray = Helper.stringToByteArray(cipherText);
		byte[] keyBytes = Helper.stringToByteArray(key);
		byte[][] decrypted = decrypt(cipherTextByteArray, keyBytes);
		long end = System.currentTimeMillis();
		OutputFileWriter writer = new OutputFileWriter();
		writer.setKey(key);
		writer.setPlainText(Helper.arrayToString(
				Helper.matrixToArray(decrypted), false));
		writer.setDecryption(false);
		writer.setStartTime(start);
		writer.setEndTime(end);
		writer.setCipherText(cipherText);
		writer.writeToFile(outputFile);
	}

	private byte[][] decrypt(byte[] cipherTextByteArray, byte[] key) {
		// TODO: Andrew: We need to invert the key somehow. Do you know how that
		// works?

		byte[][] input = Helper.arrayToMatrix(cipherTextByteArray);
		AddRoundKey roundKeyAdder = new AddRoundKey();
		SubstituteBytes byteSubstituter = new SubstituteBytes();
		ShiftRows rowShifter = new ShiftRows();
		MixColumns columnMixer = new MixColumns();

		byte[][] output;
		output = roundKeyAdder.addRoundKey(input, key);
		for (int round = 0; round < 10; round++) {
			output = byteSubstituter.substituteBytes(output, true);
			output = rowShifter.shiftRows(output, true);
			// TODO: ANDREW
			// output = columnMixer.
			output = roundKeyAdder.addRoundKey(output, key);
		}
		return output;
	}

}
