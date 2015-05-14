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

	public AddRoundKey(byte[] key) {
		subkeys = generateSubkeys(key);
	}

	private static byte[][] generateSubkeys(byte[] key) {
		byte[][] subkeys = new byte[11][16];
		subkeys[0] = key;

		for (int round = 1; round < 11; round++) {
			byte subkey[] = new byte[16];
			subkeys[round] = subkey;
		}

		// TODO
		return subkeys;
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

	private byte[] rotate(byte[] input) {
		byte[] output = new byte[input.length];
		byte a = input[0];
		for (int i = 0; i < 3; i++)
			output[i] = output[i + 1];
		output[3] = a;
		return output;
	}

}
