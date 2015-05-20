package au.com.uon.comp3260;


/**
 * 
 * This class can add a round key used in AES Encryption / Decryption
 * 
 * @author Felix Behrendt
 * 
 */
public class AddRoundKey {

	private final byte[][] subkeys;

	public AddRoundKey(byte[] key, boolean decrypt) {
		subkeys = generateSubkeys(key, decrypt);
	}

	private static byte[][] generateSubkeys(byte[] key, boolean decrypt) {
		byte[][] subkeys = new byte[11][16];
		subkeys[0] = key;

		for (int round = 1; round < 11; round++) {
			byte subkey[] = new byte[16];
			subkeys[round] = subkey;
		}

		// Key expansion

		int round = 0;
		int rounds = 10;
		int i = 16;
		byte[] temp = new byte[4];

		while (i < 176) { // need 176 bytes of key

			// set next set of temp bytes
			for (int j = 0; j < 4; j++) {
				temp[j] = subkeys[round][(j + i - 4) - (round * 16)]; // tempBytes
																		// =
																		// last
																		// 4
																		// bytes
																		// of
																		// previous
																		// subkey
				// for Round 0: (i = 16)
				// tempBytes[0] = subkeys[0][12]
				// tempBytes[1] = subkeys[0][13]
				// tempBytes[2] = subkeys[0][14]
				// tempBytes[3] = subkeys[0][15]
				// for Round 1: (i = 32)
				// temp[0] = subkeys[1][12]
				// temp[0] = subkeys[1][13]
				// ...
			}

			// if i / 16 gives no remainder = new Key
			if (i % 16 == 0) {

				// Rotate tempBytes
				temp = rotate(temp);

				// Apply S-box
				for (int j = 0; j < 4; j++) {
					temp[j] = applySbox(temp[j], decrypt);
				}

				// XOR 1st bit of temp with rcon(i)
				temp[0] ^= rcon(i);

				round++;
			}

			// set next 4 bytes of subkey
			for (int j = 0; j < 4; j++) {

				// set next 4 values in subkeys

				// change temp from byte to int
				int tempInt = temp[j];

				// change subkey value from byte to int
				int subkeyInt = subkeys[round - 2][i - ((round - 1) * 16)];

				int subkey = subkeyInt ^ tempInt;
				i++;

				// convert from int to byte and store
				subkeys[round - 1][i - ((round - 1) * 16)] = (byte) subkey;
			}
		}

		return subkeys;
	}

	private static byte applySbox(byte input, boolean decrypt) {
		char[] matrix = SubstituteBytes.subByteMatrix;
		if (decrypt) {
			// Use the inverted matrix when decrypting
			matrix = SubstituteBytes.inversedSubByteMatrix;
		}
		return (byte) matrix[input & 0xFF];
	}

	private static int rcon(int input) {
		int x = 1;
		// if input is 0, return 0
		if (input == 0) {
			x = 0;
		} else {
			// until input = 1, multiply x by 2
			while (input != 1) {
				x = multiply2(x);
				input--;
			}
		}
		return x;
	}

	private static int multiply2(int input) {

		// change input to string
		String binaryInput = Integer.toBinaryString(input);

		// multiply by 2
		StringBuilder sb = new StringBuilder();

		// shift binary left by 1
		for (int i = 1; i < 8; i++) { // ignore first digit
			sb.append(binaryInput.charAt(i));
		}
		sb.append("0"); // add last digit
		String outputString = sb.toString();

		String oneChar = "1";
		// if 1st digit of original value is a 1, run binaryXOR
		if (binaryInput.charAt(0) == oneChar.charAt(0)) {
			outputString = binaryXOR(outputString, "11011");
		}

		// change string to int
		int output = Integer.parseInt(outputString, 2);
		return output;
	}

	// XOR two binary Strings
	private static String binaryXOR(String bin1, String bin2) {
		StringBuilder sb = new StringBuilder();

		// add missing zeros if required
		int missingZeros1 = 8 - bin1.length();
		int missingZeros2 = 8 - bin2.length();
		for (int i = 0; i < missingZeros1; i++) {
			bin1 = "0" + bin1;
		}
		for (int i = 0; i < missingZeros2; i++) {
			bin2 = "0" + bin2;
		}

		// XOR both Strings
		for (int i = 0; i < 8; i++) {
			if (bin1.charAt(i) == bin2.charAt(i)) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		return sb.toString();
	}

	public byte[][] addRoundKey(byte[][] matrix, int round, boolean decrypt) {
		if (decrypt)
			round = 10 - round;
		byte[] key = subkeys[round];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[j][i] ^= key[i * 4 + j];
		return matrix;
	}

	private static byte[] rotate(byte[] input) {
		byte[] output = new byte[input.length];
		byte a = input[0];
		for (int i = 0; i < 3; i++)
			output[i] = output[i + 1];
		output[3] = a;
		return output;
	}

}
