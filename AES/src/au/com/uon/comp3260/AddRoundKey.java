package au.com.uon.comp3260;

/**
 * 
 * This class can add a round key used in AES Encryption / Decryption
 * 
 * @author Felix Behrendt
 * 
 */
public class AddRoundKey {

	private byte[][] addRoundKey(byte[][] matrix, byte[] key) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[j][i] ^= key[i * 4 + j];
		return matrix;
	}

}
