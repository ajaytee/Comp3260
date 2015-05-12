package au.com.uon.comp3260.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This is a helper class for reading input files and converting arrays and
 * matrixes
 * 
 * @author Felix Behrendt & Andrew Thursby
 * 
 */
public class Helper {

	private static String readLine(Path inputFile, int lineNbr) {
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

	public static byte[] stringToByteArray(String binaryString) {
		byte[] output = new byte[16];
		for (int i = 0; i < 16; i++) {
			String part = binaryString.substring(i * 8, (i + 1) * 8);
			output[i] = (byte) Integer.parseInt(part, 2);
		}
		return output;
	}

	public static byte[][] convertArrayToMatrix(byte[] array) {
		byte[][] hexMatrix = new byte[4][4];
		int k = 0;
		for (int i = 0; i < 4; i++) { // i = row #
			for (int j = 0; j < 4; j++) { // j = column #
				hexMatrix[j][i] = array[k];
				k++;
			}
		}
		return hexMatrix;
	}

	public static byte[] convertMatrixToArray(byte[][] matrix) {
		byte[] array = new byte[16];
		int k = 0;
		for (int i = 0; i < 4; i++) { // i = row #
			for (int j = 0; j < 4; j++) { // j = column #
				array[k] = matrix[j][i];
				k++;
			}
		}
		return array;
	}

	public static String matrixToString(byte[][] matrix, boolean pretty) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) { // i = row #
			for (int j = 0; j < 4; j++) { // j = column #
				Byte b = matrix[j][i];
				sb.append(stringForByte(b) + " ");
				if (pretty)
					sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static String arrayToString(byte[] array, boolean pretty) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < 16; j++) {
			Byte b = array[j];
			sb.append(stringForByte(b));
			if (pretty)
				sb.append(" ");
		}
		return sb.toString();
	}

	public static String stringForByte(byte b) {
		String value = Integer.toBinaryString(Byte.toUnsignedInt(b));
		int missingZeros = 8 - value.length();
		for (int i = 0; i < missingZeros; i++) {
			value = "0" + value;
		}
		return value;
	}

	/**
	 * Compares to byte arrays and returns the number of bits that are different
	 * 
	 * @param original
	 * @param newText
	 * @return int > 0
	 */
	public static int numberOfDifferentBits(byte[] original, byte[] newText) {
		// It is very easy to compare to strings
		String org = arrayToString(original, false);
		String newTex = arrayToString(newText, false);
		int diff = 0;
		for (int i = 0; i < org.length(); i++) {
			if (org.charAt(i) != newTex.charAt(i)) {
				diff++;
			}
		}
		return diff;
	}

}
