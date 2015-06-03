package au.com.uon.comp3260;

import au.com.uon.comp3260.util.Helper;

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

		byte[] keys = new byte[176];

		for (int round = 1; round < 11; round++) {
			byte subkey[] = new byte[16];
			subkeys[round] = subkey;
		}

		// Key Expansion

		// set first 16 bytes
		for (int i = 0; i < 16; i++) {
			keys[i] = key[i];
		}

		int round = 1;
		int i = 16; // first 16 bytes already set
		byte[] temp = new byte[4];

		while (i < 176) { // need 176 bytes of keys

			// set temp bytes
			for (int j = 0; j < 4; j++) {
				temp[j] = keys[j + i - 4];
			}

			// if new key, do main calculations
			if (i % 16 == 0) {

				// Rotate tempBytes
				temp = rotate(temp);

				// Apply S-box
				for (int j = 0; j < 4; j++) {
					// We always want the encryption sbox. for decryption we use
					// the same keys, but in inverse order.
					temp[j] = applySbox(temp[j], false);
				}

				// XOR 1st bit of temp with round constant
				temp[0] ^= rcon(round);
				round++;
			}

			byte newValue = 0;

			// assign temp bytes to keys
			for (int j = 0; j < 4; j++)

				// set new value
				newValue = (byte) (temp[j] ^ keys[i - 16]);

			// store new value
			keys[i] = newValue;

			i++;
		}

		// convert keys into subkeys arrays
		int total = 16;
		byte[] newRoundKey = new byte[16];

		for (int rnd = 1; rnd < 11; rnd++) {

			// set new key value
			for (int j = 0; j < 16; j++) {
				newRoundKey[j] = keys[total];
				total++;
			}

			// set subkeys to new key value
			subkeys[rnd] = newRoundKey;

			byte[][] testMatrix = Helper.arrayToMatrix(newRoundKey);
			String testString = Helper.matrixToHexString(testMatrix);
			System.out.println("Subkey Round " + rnd + " - " + testString);
			// System.out.println("SUBKEY ROUND: " + rnd + " -- " +
			// newRoundKey);

		}
		// System.out.println(Helper.matrixToHexString(subkeys));
		return subkeys;
	}

	/*
	 * // OLD: Key expansion
	 * 
	 * int round = 0; int i = 16; byte[] temp = new byte[4];
	 * 
	 * while (i < 176) { // need 176 bytes of key
	 * 
	 * // if new key, set temp to last 4 bytes in previous subkey // if not new
	 * key, set temp to // set next set of temp bytes for (int j = 0; j < 4;
	 * j++) { temp[j] = subkeys[round][(j + i - 4) - (round * 16)]; // tempBytes
	 * // = // last // 4 // bytes // of // previous // subkey // for Round 0: (i
	 * = 16) // tempBytes[0] = subkeys[0][12] // tempBytes[1] = subkeys[0][13]
	 * // tempBytes[2] = subkeys[0][14] // tempBytes[3] = subkeys[0][15] // for
	 * Round 1: (i = 32) // temp[0] = subkeys[1][12] // temp[0] = subkeys[1][13]
	 * // ... }
	 * 
	 * // if i / 16 gives no remainder = new Key = do more stuff if (i % 16 ==
	 * 0) {
	 * 
	 * // Rotate tempBytes temp = rotate(temp);
	 * 
	 * // Apply S-box for (int j = 0; j < 4; j++) { temp[j] = applySbox(temp[j],
	 * decrypt); }
	 * 
	 * // XOR 1st bit of temp with rcon(i) temp[0] ^= rcon(round);
	 * 
	 * round++; }
	 * 
	 * // set next 4 bytes of subkey for (int j = 0; j < 4; j++) {
	 * 
	 * // set next 4 values in subkeys
	 * 
	 * // change temp from byte to int int tempInt = temp[j];
	 * 
	 * // change subkey value (from previous key) from byte to int int subkeyInt
	 * = subkeys[round - 1][i - (round * 16)];
	 * 
	 * int subkey = subkeyInt ^ tempInt;
	 * 
	 * // convert from int to byte and store subkeys[round][i - (round * 16)] =
	 * (byte) subkey;
	 * 
	 * i++; } }
	 * 
	 * return subkeys; }
	 */

	private static byte applySbox(byte input, boolean decrypt) {
		char[] matrix = SubstituteBytes.subByteMatrix;
		if (decrypt) {
			// Use the inverted matrix when decrypting
			matrix = SubstituteBytes.inversedSubByteMatrix;
		}
		return (byte) matrix[input & 0xFF];
	}

	private static int rcon2(int input) {
		int result = 0;
		switch (input) {
		case 1:
			result = 1;
		case 2:
			result = 2;
		case 3:
			result = 4;
		case 4:
			result = 8;
		case 5:
			result = 16;
		case 6:
			result = 32;
		case 7:
			result = 64;
		case 8:
			result = 128;
		case 9:
			result = 29;
		case 10:
			result = 54;
		}

		return result;
	}

	private static int rcon(int input) {
		int x = 1;
		// if input is 0, return 0
		if (input == 0) {
			x = 0;
		} else {
			// until input = 1, multiply x by 2
			while (input != 1) {
				x = multiply((byte) x, (byte) 2);
				input--;
			}
		}
		return x;
	}

	// Multiplies two bytes in garlois field 2^8
	private static byte multiply(byte a, byte b) {
		byte returnValue = 0;
		byte temp = 0;
		while (a != 0) {
			if ((a & 1) != 0)
				returnValue = (byte) (returnValue ^ b);
			temp = (byte) (b & 0x80);
			b = (byte) (b << 1);
			if (temp != 0)
				b = (byte) (b ^ 0x1b);
			a = (byte) ((a & 0xff) >> 1);
		}
		return returnValue;
	}

	/*
	 * private static int multiply2(int input) {
	 * 
	 * // change input to string String binaryInput =
	 * Integer.toBinaryString(input);
	 * 
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * // shift binary left by 1 for (int i = 1; i < binaryInput.length(); i++)
	 * { // ignore first digit sb.append(binaryInput.charAt(i)); }
	 * sb.append("0"); // add last digit String outputString = sb.toString();
	 * 
	 * String oneChar = "1"; // if 1st digit of original value is a 1, run
	 * binaryXOR if (binaryInput.charAt(0) == oneChar.charAt(0)) { outputString
	 * = binaryXOR(outputString, "11011"); }
	 * 
	 * // change string to int int output = Integer.parseInt(outputString, 2);
	 * return output; }
	 * 
	 * // XOR two binary Strings private static String binaryXOR(String bin1,
	 * String bin2) { StringBuilder sb = new StringBuilder();
	 * 
	 * // add missing zeros if required int missingZeros1 = 8 - bin1.length();
	 * int missingZeros2 = 8 - bin2.length(); for (int i = 0; i < missingZeros1;
	 * i++) { bin1 = "0" + bin1; } for (int i = 0; i < missingZeros2; i++) {
	 * bin2 = "0" + bin2; }
	 * 
	 * // XOR both Strings for (int i = 0; i < 8; i++) { if (bin1.charAt(i) ==
	 * bin2.charAt(i)) { sb.append("0"); } else { sb.append("1"); } } return
	 * sb.toString(); }
	 */
	public byte[][] addRoundKey(byte[][] matrix, int round, boolean decrypt) {
		if (decrypt)
			round = 10 - round;
		byte[] key = subkeys[round];
		byte[][] output = new byte[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				output[j][i] = (byte) (matrix[j][i] ^ key[i * 4 + j]);
		return output;
	}

	private static byte[] rotate(byte[] input) {
		byte[] output = new byte[input.length];
		byte a = input[0];
		for (int i = 0; i < 3; i++)
			output[i] = input[i + 1];
		output[3] = a;
		return output;
	}

}
