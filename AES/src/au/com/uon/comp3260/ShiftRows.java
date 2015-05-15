package au.com.uon.comp3260;

/**
 * This class can shift rows used in AES Encryption / Decryption
 * 
 * @author Andrew Thursby
 * 
 */
public class ShiftRows {

	public byte[][] shiftRows(byte[][] input, boolean decrypt) {
		byte[] temp = new byte[4];
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (decrypt) {
					temp[(j + i) % 4] = input[i][j];
				} else {
					temp[j] = input[i][(j + i) % 4];
				}
			}
			for (int j = 0; j < 4; j++)
				input[i][j] = temp[j];
		}
		return input;

	}
}
